using BatemanCafeteria.Models;
using BatemanCafeteria.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using System.Web;
using System.Web.Mvc;
using System.Net;
using System.Data;

namespace BatemanCafeteria.Controllers
{
    [Authorize(Roles = "Caf_Admin, Caf_Secretary")]
    public class SecretaryController : Controller
    {
        ApplicationDbContext applicationDbContext = new ApplicationDbContext();
        // GET: Secretary
        public ActionResult Index()
        {
            return View();
        }

        [HttpGet]
        public ActionResult ManageOrders()
        {
            if(TempData["Errors"] != null)
            {
                ViewBag.Errors = TempData["Errors"].ToString();
            }
            applicationDbContext = new ApplicationDbContext();
            ManageOrdersViewModel manageOrders = new ManageOrdersViewModel
            {
                Received = applicationDbContext.Caf_Invoices.Where(items => items.StatusId == 1).ToList(),
                Cooking = applicationDbContext.Caf_Invoices.Where(items => items.StatusId == 2).ToList(),
                ReadyForPickup = applicationDbContext.Caf_Invoices.Where(items => items.StatusId == 3).ToList()
                
            };
            return View(manageOrders);
        }

        public ActionResult RefreshOrders()
        {
            ManageOrdersViewModel manageOrders = new ManageOrdersViewModel
            {
                Received = applicationDbContext.Caf_Invoices.Where(items => items.StatusId == 1).ToList(),
                Cooking = applicationDbContext.Caf_Invoices.Where(items => items.StatusId == 2).ToList(),
                ReadyForPickup = applicationDbContext.Caf_Invoices.Where(items => items.StatusId == 3).ToList()

            };
            return PartialView("_ManageOrdersRefresh", manageOrders);
        }

        public ActionResult ChangeStatus(int id)
        {
            Caf_InvoiceModel invoice = applicationDbContext.Caf_Invoices.Where(item => item.InvoiceID == id).First();
            if (invoice.StatusId < 3)
            {
                invoice.StatusId++;
                applicationDbContext.SaveChanges();
            }
            return RedirectToAction("ManageOrders");
        }

        public ActionResult ChangePaymentStatus(int id)
        {
            Caf_InvoiceModel invoice = applicationDbContext.Caf_Invoices.Where(item => item.InvoiceID == id).First();
            if (invoice.Payment_status == false)
            {
                invoice.Payment_status = true;
            }
            else if (invoice.Payment_status == true)
            {
                applicationDbContext.Caf_Invoices.Remove(invoice);
                EmailHelper email = new EmailHelper();
                email.sendEmail(invoice.Customer_email,
                    "Your order is ready for pickup! Be sure to bring $" + invoice.Order_total.ToString() + " with you.",
                    "Your order is ready for pickup!",
                    invoice.Customer_name);
            }
            applicationDbContext.SaveChanges();
            return RedirectToAction("ManageOrders");
            
        }
        
        [HttpGet]
        public ActionResult ViewOrder(int id)
        {
            var invoice = applicationDbContext.Caf_Invoices.Find(id);
            var items = applicationDbContext.Caf_OrderItems.Where(x => x.InvoiceID == id).ToList();
            ViewOrderViewModel viewOrder = new ViewOrderViewModel
            {
                OrderId = invoice.InvoiceID,
                Email = invoice.Customer_email,
                Name = invoice.Customer_name,
                Phone = invoice.Customer_phone,
                Date = invoice.Order_date,
                Time = invoice.Order_time,
                Total = invoice.Order_total,
                Items = items,
                Username = invoice.Username
            };
            if (applicationDbContext.Caf_Caterings.Where(x => x.InvoiceID == id).Any())
            {
                viewOrder.CateringInfo = applicationDbContext.Caf_Caterings.Where(x => x.InvoiceID == id).First();
                viewOrder.Room = applicationDbContext.res_rooms.Where(x => x.room_id == viewOrder.CateringInfo.Reservation.room_id).First();
            }

            return PartialView("_ViewOrder", viewOrder);
        }

        [HttpGet]
        public ActionResult DeleteOrder(int ? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            else
            {
                var invoice = applicationDbContext.Caf_Invoices.Find(id);
                if(invoice == null)
                {
                    return HttpNotFound();
                }
                return PartialView("_Delete", invoice);
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteOrder(int id)
        {
            var invoice = applicationDbContext.Caf_Invoices.Find(id);
            try
            {
                applicationDbContext.Caf_Invoices.Remove(invoice);
                applicationDbContext.SaveChanges();
            }
            catch(DataException)
            {
                return RedirectToAction("ManageOrders");
            }
            return RedirectToAction("ManageOrders");
        }

        [HttpGet]
        public ActionResult Edit (int id)
        {
            var invoice = applicationDbContext.Caf_Invoices.Find(id);
            var items = applicationDbContext.Caf_OrderItems.Where(x => x.InvoiceID == id).ToList();
            if (invoice != null)
            {
                var viewModel = new ViewOrderViewModel
                {
                    OrderId = invoice.InvoiceID,
                    Phone = invoice.Customer_phone,
                    Name = invoice.Customer_name,
                    Email = invoice.Customer_email,
                    Date = invoice.Order_date,
                    Time = invoice.Order_time,
                    Total = invoice.Order_total,
                    Items = items
                };
                return View("_Edit", viewModel);
            }
            else
            {
                TempData["Errors"] = "Could not find order. Please try again.";
                return RedirectToAction("ManageOrders");
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit(ViewOrderViewModel editedOrder)
        {
            try
            {
                Caf_InvoiceModel invoice = applicationDbContext.Caf_Invoices.Find(editedOrder.OrderId);
                invoice.Customer_email = editedOrder.Email;
                invoice.Customer_name = editedOrder.Name;
                invoice.Customer_phone = editedOrder.Phone;
                invoice.Order_total = editedOrder.Total;
                applicationDbContext.Entry(invoice).State = EntityState.Modified;
                applicationDbContext.SaveChanges();
                UpdateItems(editedOrder);
                return RedirectToAction("ManageOrders");
            }
            catch(Exception e)
            {
                e.ToString();
                TempData["Errors"] = "Something went wrong. Please try again.";
                return RedirectToAction("ManageOrders");
            }
        }

        [HttpPost]
        public void UpdateItems (ViewOrderViewModel editedOrder)
        {
            List<Caf_OrderItemModel> orders = applicationDbContext.Caf_OrderItems.Where(x => x.InvoiceID == editedOrder.OrderId).ToList();
            int index = 0;
            foreach (var item in orders)
            {
                item.Quantity = editedOrder.Items[index].Quantity;
                item.Special_instructions = editedOrder.Items[index].Special_instructions;
                index++;
                applicationDbContext.Entry(item).State = EntityState.Modified;
            }
            applicationDbContext.SaveChanges();
        }


        [HttpGet]
        public ActionResult CateringOrder()
        {
            return RedirectToAction("Index", "Home");
        }

        [HttpGet]
        public ActionResult BanUser(string userName)
        {
            Caf_ServiceUsers user = applicationDbContext.Caf_ServiceUsers.Where(x => x.Username == userName).FirstOrDefault();
            if(user != null)
            {
                return View(user);
            }
            else
            {
                TempData["Errors"] = "Could not complete request. Please try again.";
                return RedirectToAction("ManageOrders");
            }
        }

        [HttpPost]
        public ActionResult BanUser(Caf_ServiceUsers user)
        {
            if (ModelState.IsValid)
            {
                user = applicationDbContext.Caf_ServiceUsers.Where(x => x.Username == user.Username).FirstOrDefault();
                user.IsBanned = true;
                applicationDbContext.SaveChanges();
                EmailHelper emailHelper = new EmailHelper();
                string email = applicationDbContext.Caf_Invoices.Where(x => x.Username == user.Username).First().Customer_email;
                emailHelper.sendEmail(email, "Due to misuse of the service, you have been banned until further notice. You may still order in person. If you feel like this is a mistake, contact the system admin.", "Cafeteria Service Ban", email);
                return RedirectToAction("ManageOrders");
            }
            else
            {
                TempData["Errors"] = "Something went wrong. Please try again.";
                return RedirectToAction("ManageOrders");
            }
        }

    }
}