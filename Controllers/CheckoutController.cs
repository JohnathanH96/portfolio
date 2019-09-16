using BatemanCafeteria.Models;
using System;
using System.Diagnostics;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web.Mvc;
using BatemanCafeteria.ViewModels;
using System.Collections.Generic;

namespace BatemanCafeteria.Controllers
{
    public class CheckoutController : Controller
    {
        ApplicationDbContext applicationDbContext = new ApplicationDbContext();
        // GET: Checkout
        public ActionResult Checkout()
        {
            string userName = Environment.UserName.ToString();
            Caf_ServiceUsers user = applicationDbContext.Caf_ServiceUsers.Where(x => x.Username == userName).FirstOrDefault();
            if (user == null)
            {
                Caf_ServiceUsers newUser = new Caf_ServiceUsers
                {
                    DomainName = Environment.UserDomainName,
                    MachineName = Environment.MachineName,
                    Username = Environment.UserName,
                    IsBanned = false
                };
                applicationDbContext.Caf_ServiceUsers.Add(newUser);
                applicationDbContext.SaveChanges();
                var cart = ShoppingCart.GetCart(this.HttpContext);
                if (cart.GetCartItems().Count() == 0)
                {
                    return RedirectToAction("CartView", "ShoppingCart");
                }
                else
                {
                    return View();
                }
            }
            else if(user.IsBanned == false)
            {
                user.DomainName = Environment.UserDomainName;
                user.MachineName = Environment.MachineName;
                applicationDbContext.SaveChanges();
                var cart = ShoppingCart.GetCart(this.HttpContext);
                if (cart.GetCartItems().Count() == 0)
                {
                    return RedirectToAction("CartView", "ShoppingCart");
                }
                else
                {
                    return View();
                }
            }
            else
            {
                user.DomainName = Environment.UserDomainName;
                user.MachineName = Environment.MachineName;
                applicationDbContext.SaveChanges();
                TempData["Errors"] = "You have been banned from using this system due to misuse. If you believe this is an error, contact your system admin.";
                return RedirectToAction("Index", "Home");
            }

        }

        [HttpGet]
        [Authorize(Roles = "Caf_Secretary")]
        public ActionResult CateringCheckout(int ? verification)
        {
            if(verification == null)
            {
                TempData["Errors"] = "Could not verify catering order. Please try again.";
                return RedirectToAction("CartView", "ShoppingCart");
            }
            try
            {
                var resInfo = applicationDbContext.res_reservations.Where(x => x.ver_code == verification).First();
                Caf_InvoiceModel invoice = new Caf_InvoiceModel
                {
                    Customer_email = resInfo.email_addr,
                    Customer_name = resInfo.res_name,
                    Customer_phone = resInfo.phone_ext,
                    Order_total = 0.00M,
                    Payment_status = true

                };
                CateringCheckoutViewModel cateringModel = new CateringCheckoutViewModel
                {
                    Reservation = resInfo,
                    Room = applicationDbContext.res_rooms.Find(resInfo.room_id),
                    Invoice = invoice,
                    SelectedTime = resInfo.res_start
                };
                return View(cateringModel);
            }
            catch
            {
                TempData["Errors"] = "Could not create catering order. Please try again.";
                return RedirectToAction("CartView", "ShoppingCart");

            }
        }

        [HttpPost]
        [Authorize(Roles = "Caf_Secretary")]
        public ActionResult CateringCheckout(CateringCheckoutViewModel cateringModel)
        {
            if (ModelState.IsValid)
            {
                DateTime start = cateringModel.Reservation.res_start;
                DateTime end = cateringModel.Reservation.res_end;

                DateTime selected = new DateTime(cateringModel.Reservation.res_dt.Year, cateringModel.Reservation.res_dt.Month, cateringModel.Reservation.res_dt.Day, 
                                                cateringModel.SelectedTime.Hour, cateringModel.SelectedTime.Minute, 0);

                int startVal = DateTime.Compare(selected, start);
                int endVal = DateTime.Compare(selected, end);
                if (startVal < 0 || endVal > 0)
                {
                    var resInfo = applicationDbContext.res_reservations.Where(x => x.res_id == cateringModel.Reservation.res_id).First();
                    Caf_InvoiceModel invoice = new Caf_InvoiceModel
                    {
                        Customer_email = resInfo.email_addr,
                        Customer_name = resInfo.res_name,
                        Customer_phone = resInfo.phone_ext,
                        Order_total = 0.00M,
                        Payment_status = true

                    };
                    cateringModel = new CateringCheckoutViewModel
                    {
                        Reservation = resInfo,
                        Room = applicationDbContext.res_rooms.Find(resInfo.room_id),
                        Invoice = invoice,
                        SelectedTime = resInfo.res_start
                    };
                    ModelState.AddModelError("", "Time out of bounds. Please enter a time within the reservation time frame.");
                    return View(cateringModel);
                }
                else if (startVal >= 0 && endVal <= 0)
                {
                    Caf_Caterings catering = new Caf_Caterings
                    {
                        InvoiceID = cateringModel.Invoice.InvoiceID,
                        res_id = cateringModel.Reservation.res_id,
                        Instructions = cateringModel.Instructions,
                        Time = cateringModel.SelectedTime
                    };
                    Caf_InvoiceModel invoice = cateringModel.Invoice;
                    invoice.StatusId = 1;
                    invoice.Order_date = DateTime.Now.ToShortDateString();
                    invoice.Order_time = DateTime.Now.ToShortTimeString();
                    var cart = ShoppingCart.GetCart(this.HttpContext);
                    applicationDbContext.Caf_Invoices.Add(invoice);
                    applicationDbContext.Caf_Caterings.Add(catering);
                    applicationDbContext.SaveChanges();
                    cart.CreateOrder(invoice);
                    return RedirectToAction("Complete", new { id = invoice.InvoiceID });
                }
                else
                {
                    var resInfo = applicationDbContext.res_reservations.Where(x => x.res_id == cateringModel.Reservation.res_id).First();
                    Caf_InvoiceModel invoice = new Caf_InvoiceModel
                    {
                        Customer_email = resInfo.email_addr,
                        Customer_name = resInfo.res_name,
                        Customer_phone = resInfo.phone_ext,
                        Order_total = 0.00M,
                        Payment_status = true

                    };
                    cateringModel = new CateringCheckoutViewModel
                    {
                        Reservation = resInfo,
                        Room = applicationDbContext.res_rooms.Find(resInfo.room_id),
                        Invoice = invoice,
                        SelectedTime = resInfo.res_start
                    };

                    ModelState.AddModelError("", "Something went wrong. Please try again.");
                    return View(cateringModel);
                }

            }else
            {
                var resInfo = applicationDbContext.res_reservations.Where(x => x.res_id == cateringModel.Reservation.res_id).First();
                Caf_InvoiceModel invoice = new Caf_InvoiceModel
                {
                    Customer_email = resInfo.email_addr,
                    Customer_name = resInfo.res_name,
                    Customer_phone = resInfo.phone_ext,
                    Order_total = 0.00M,
                    Payment_status = true

                };
                cateringModel = new CateringCheckoutViewModel
                {
                    Reservation = resInfo,
                    Room = applicationDbContext.res_rooms.Find(resInfo.room_id),
                    Invoice = invoice,
                    SelectedTime = resInfo.res_start
                };

                ModelState.AddModelError("", "Oops! Something went wrong. Please try again.");
                return View(cateringModel);
            }
        }

        [HttpGet]
        [Authorize(Roles = "Caf_Secretary")]
        public ActionResult CateringVerification()
        {
            return PartialView("_CateringVerification");
        }

        [HttpPost]
        [Authorize(Roles = "Caf_Secretary")]
        [ValidateAntiForgeryToken]
        public ActionResult CateringVerification(CateringVerficationViewModel verificationModel)
        {
            //Check if this works
            int vCode = verificationModel.VerificationCode;
            if(applicationDbContext.res_reservations.Where(x => x.ver_code == vCode && x.approved_ind == "y").Any() && vCode != 0)
            {
                return RedirectToAction("CateringCheckout", new { verification = vCode });
            }
            else
            {
                TempData["Errors"] = "Could not verify catering order. Please try again.";
                return RedirectToAction("CartView", "ShoppingCart");
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Checkout(Caf_InvoiceModel invoice)
        {
            if(applicationDbContext.Caf_ServiceUsers.Where(x => x.Username == Environment.UserName).FirstOrDefault().IsBanned)
            {
                TempData["Errors"] = "You have been banned from using this system due to misuse. If you believe this is an error, contact your system admin.";
                return RedirectToAction("Index", "Home");
            }
            if (ModelState.IsValid)
            {
                var cart = ShoppingCart.GetCart(this.HttpContext);
                if (cart.GetCount() == 0)
                {
                    ViewBag.Errors("You appear to have an empty cart. Please make sure you put items in your cart.");
                    return View(invoice);
                }
                invoice.StatusId = 1;
                invoice.Order_date = DateTime.Now.ToShortDateString();
                invoice.Order_time = DateTime.Now.ToShortTimeString();
                string pattern = @"^(?:\(?)(?<AreaCode>\d{3})(?:[\).\s]?)(\s?)(?<Prefix>\d{3})(?:[-\.\s]?)(?<Suffix>\d{4})(?!\d)";
                string extension = @"^\d{1,5}$";
                Match match = Regex.Match(invoice.Customer_phone, pattern);
                Match matchEx = Regex.Match(invoice.Customer_phone, extension);
                if (!match.Success && !matchEx.Success)
                {
                    ModelState.AddModelError("", "Incorrect phone number or extension format");
                    return View(invoice);
                }else if (match.Success)
                {
                    string phone = Regex.Replace(invoice.Customer_phone, "[^0-9]", "");
                    phone = String.Format("{0:(###) ###-####}", Convert.ToInt64(phone));
                    invoice.Customer_phone = phone;
                }
                invoice.Payment_status = false;
                invoice.Order_total = cart.GetTotal();
                invoice.Username = Environment.UserName;
                
                applicationDbContext.Caf_Invoices.Add(invoice);
                applicationDbContext.SaveChanges();
                cart.CreateOrder(invoice);
                return RedirectToAction("Complete", new { id = invoice.InvoiceID });
            }
            else
            {
                ViewBag.Errors("Sorry, something went wrong. Please fill out all fields and check to make sure your cart is not empty.");
                return View(invoice);
            }
        }

        [HttpGet]
        public ActionResult Complete(int id)
        {
            bool isValid = applicationDbContext.Caf_Invoices.Where(x => x.InvoiceID == id).Any();
            if (isValid)
            {
                ViewBag.Id = id;
                return View();
            }
            else
            {
                ViewBag.Errors = "Oops, something went wrong in your request. Please try again.";
                return View();
            }
        }

    }
}
