using BatemanCafeteria.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using BatemanCafeteria.ViewModels;

namespace BatemanCafeteria.Controllers
{
    public class HomeController : Controller
    {
        ApplicationDbContext applicationDbContext = new ApplicationDbContext();
        public ActionResult Index()
        {
            if(TempData["Errors"] != null)
            {
                ViewBag.Errors = TempData["Errors"].ToString();
            }
            if (User.IsInRole("Caf_Admin"))
            {
                return RedirectToAction("Index", "Admin");
            }
            return View();
        }

        public ActionResult Cart()
        {

            return View();
        }

        public ActionResult Contact()
        {

            return View();
        }

        public ActionResult OrderStatus()
        {
            return View();
        }

        public ActionResult GetOrderStatus(OrderStatusViewModel orderID)
        {
            if (ModelState.IsValid && applicationDbContext.Caf_Invoices.Where(x => x.InvoiceID == orderID.Id).Any())
            {
                int statusID = applicationDbContext.Caf_Invoices.Find(orderID.Id).StatusId;
                ViewBag.OrderNum = orderID.Id;
                return PartialView("_OrderStatus", statusID);
            }
            ViewBag.Error = "Could not find an order with that order number. Please enter the number sent to you in the confirmation email.";
            return PartialView("_OrderStatus", -1);
        }

        [HttpPost]
        public int GetStatus(int id)
        {
            int stateId = applicationDbContext.Caf_Invoices.Find(id).StatusId;
            return stateId;
        }

        public ActionResult Menu(string category)
        {
            if (category != null)
            {
                if (category != "DailySpecial")
                {
                    int catId = applicationDbContext.Caf_FoodCategories.Where(x => x.Category == category.Trim()).First().CategoryId;
                    List<Caf_MenuItemModel> menuItems = applicationDbContext.Caf_MenuItems.Where(x => x.CategoryId == catId).ToList();
                    return View(menuItems);
                }
                else
                {
                    int catId = applicationDbContext.Caf_FoodCategories.Where(x => x.Category == category.Trim()).First().CategoryId;
                    List<Caf_DailySpecials> activeItems = applicationDbContext.Caf_DailySpecials.Where(x => x.Active).ToList();
                    List<Caf_MenuItemModel> menuItems = new List<Caf_MenuItemModel>();
                    foreach(var item in activeItems)
                    {
                        menuItems.Add(applicationDbContext.Caf_MenuItems.Find(item.MenuID));
                    }
                    
                    return View(menuItems);
                }
            }
            else
            {
                return RedirectToAction("Index");
            }
        }
    }
}