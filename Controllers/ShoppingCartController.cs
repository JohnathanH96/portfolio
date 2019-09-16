using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using BatemanCafeteria.Models;
using BatemanCafeteria.ViewModels;

namespace BatemanCafeteria.Controllers
{
    public class ShoppingCartController : Controller
    {
        ApplicationDbContext applicationDbContext = new ApplicationDbContext();
        // GET: ShoppingCart
        public ActionResult CartView()
        {
            if(TempData["Errors"] != null) { ViewBag.Errors = TempData["Errors"].ToString(); }
            var cart = ShoppingCart.GetCart(this.HttpContext);
            var viewModel = new ShoppingCartViewModel
            {
                CartItems = cart.GetCartItems(),
                CartTotal = cart.GetTotal()
            };
            return View(viewModel);
        }

        [HttpGet]
        public ActionResult AddToCart(int id)
        {
            Caf_MenuItemModel menuItem = applicationDbContext.Caf_MenuItems.Where(item => item.MenuID == id).First();
            AddToCartViewModel addToCartView = new AddToCartViewModel
            {
                MenuItem = menuItem
            };
            return PartialView("_AddToCart", addToCartView);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult AddToCart(AddToCartViewModel model)
        {
            var errors = ModelState.Values.SelectMany(v => v.Errors);
            Debug.Write(errors);
            if (ModelState.IsValid)
            {
                var cart = ShoppingCart.GetCart(this.HttpContext);
                cart.AddToCart(model);
                string category = applicationDbContext.Caf_FoodCategories.Where(x => x.CategoryId == model.MenuItem.CategoryId).First().Category;
                return RedirectToAction("Menu", "Home", new { category });
            }
            else {
                
                return RedirectToAction("Index", "Home");
            }
        }

        [HttpPost]
        public ActionResult RemoveFromCart(int id)
        {
            var cart = ShoppingCart.GetCart(this.HttpContext);

            string itemName = applicationDbContext.Caf_Carts
                .Single(item => item.RecordID == id).MenuItem.Title;

            int itemCount = cart.RemoveFromCart(id);

            var results = new ShoppingCartRemoveViewModel
            {
                Message = Server.HtmlEncode(itemName) +
                " has been removed from your shopping cart.",
                CartTotal = cart.GetTotal(),
                CartCount = cart.GetCount(),
                ItemCount = itemCount,
                DeleteID = id
            };
            return Json(results);
        }

        public ActionResult EmptyCart()
        {
            var cart = ShoppingCart.GetCart(this.HttpContext);
            cart.EmptyCart();
            return RedirectToAction("CartView");
        }

        [ChildActionOnly]
        public ActionResult CartSummary()
        {
            var cart = ShoppingCart.GetCart(this.HttpContext);
            ViewData["CartCount"] = cart.GetCount();
            return PartialView("CartSummaryView");
        }
    }
}