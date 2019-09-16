using BatemanCafeteria.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Text.RegularExpressions;
using System.Web.Mvc;
using BatemanCafeteria.ViewModels;

namespace BatemanCafeteria.Controllers
{
    [Authorize(Roles ="Caf_Secretary")]
    public class MenuController : Controller
    {
        ApplicationDbContext applicationDbContext = new ApplicationDbContext();

        public ActionResult Create()
        {
            var categories = applicationDbContext.Caf_FoodCategories.ToList();
            ViewBag.Categories = new SelectList(categories, "CategoryId", "Category");
            return PartialView("_Create");
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create(Caf_MenuItemModel menuItem)
        {
            var categories = applicationDbContext.Caf_FoodCategories.ToList();
            ViewBag.Categories = new SelectList(categories, "CategoryId", "Category");
            if (ModelState.IsValid)
            {

                string filename = menuItem.Title;
                string extension = Path.GetExtension(menuItem.ImageFile.FileName);
                string category = applicationDbContext.Caf_FoodCategories.Find(menuItem.CategoryId).Category;
                
                if (!string.Equals(extension, ".jpg", StringComparison.OrdinalIgnoreCase)
                && !string.Equals(extension, ".png", StringComparison.OrdinalIgnoreCase)
                && !string.Equals(extension, ".jpeg", StringComparison.OrdinalIgnoreCase))
                {
                    TempData["Errors"] = "Selected file must be an image of type .png, .jpg";
                    return RedirectToAction("EditIndex");

                }

                try
                {
                    if (!menuItem.ImageFile.InputStream.CanRead)
                    {
                        TempData["Errors"] = "Selected file must be an image of type .png, .jpg";
                        return RedirectToAction("EditIndex");

                    }

                    if (menuItem.ImageFile.ContentLength < 512)
                    {
                        TempData["Errors"] = "Selected file must be an image of type .png, .jpg";
                        return RedirectToAction("EditIndex");

                    }

                    byte[] buffer = new byte[512];
                    menuItem.ImageFile.InputStream.Read(buffer, 0, 512);
                    string content = System.Text.Encoding.UTF8.GetString(buffer);
                    if(Regex.IsMatch(content, @"<script|<html|<head|<title|<body|<pre|<table|<a\s+href|<img|<plaintext|<cross\-domain\-policy",
                        RegexOptions.IgnoreCase | RegexOptions.CultureInvariant | RegexOptions.Multiline))
                    {
                        TempData["Errors"] = "Selected file must be an image of type .png, .jpg";
                        return RedirectToAction("EditIndex");

                    }
                }
                catch (Exception)
                {
                    ModelState.AddModelError("", "Selected file must be an image of type .png, .jpg");

                    return View(menuItem);
                }
                filename = filename + extension;
                menuItem.ImgLocation = "/Images/MenuItems/" + filename;
                filename = Path.Combine(Server.MapPath("/Images/MenuItems"), filename);
                menuItem.ImageFile.SaveAs(filename);
                try
                {
                    applicationDbContext.Caf_MenuItems.Add(menuItem);
                    applicationDbContext.SaveChanges();
                }
                catch (System.Data.Entity.Infrastructure.DbUpdateException)
                {
                    TempData["Errors"] = "Error in creating menu item. " + menuItem.Title + " already exists.";
                    return RedirectToAction("EditIndex");
                }
                if (menuItem.FoodCategory.Category.Equals("DailySpecial"))
                {
                    ToggleDailySpecial(menuItem.MenuID);
                }
                return RedirectToAction("EditIndex", "Menu", new { category = category });
            }
            TempData["Errors"] = "Error in creating menu item. Please try again.";
            return RedirectToAction("EditIndex");
        }

        [HttpGet]
        public ActionResult Delete(int ? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            else
            {
                Caf_MenuItemModel menuItem = applicationDbContext.Caf_MenuItems.Find(id);
                if(menuItem == null)
                {
                    return HttpNotFound();
                }
                return PartialView("_Delete", menuItem);
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Delete(int id)
        {
            Caf_MenuItemModel menuItemModel = applicationDbContext.Caf_MenuItems.Find(id);
            string category = menuItemModel.FoodCategory.Category;
            try
            {
                applicationDbContext.Caf_MenuItems.Remove(menuItemModel);
                applicationDbContext.SaveChanges();
            }
            catch(DataException)
            {
                TempData["Errors"] = "Could not delete item. Please try again.";
                return RedirectToAction("EditIndex");
            }
            return RedirectToAction("Menu", "Home", new { category = category });
        }

        [HttpGet]
        public ActionResult Edit (int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            else
            {
                Caf_MenuItemModel menuItem = applicationDbContext.Caf_MenuItems.Find(id);
                if (menuItem == null)
                {
                    return HttpNotFound();
                }
                var categories = applicationDbContext.Caf_FoodCategories.ToList();
                ViewBag.Categories = new SelectList(categories, "CategoryId", "Category");
                return PartialView("_Edit", menuItem);
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit (Caf_MenuItemModel menuItem)
        {
            if (ModelState.IsValid)
            {
                applicationDbContext.Entry(menuItem).State = EntityState.Modified;
                applicationDbContext.SaveChanges();
                string category = applicationDbContext.Caf_FoodCategories.Find(menuItem.CategoryId).Category;

                return RedirectToAction("Menu", "Home", new { category});
            }
            TempData["Errors"] = "Something went wrong. Please try again.";
            return RedirectToAction("EditIndex");
        }


        [HttpGet]
        public ActionResult EditIndex()
        {
            if (TempData["Errors"] != null) { ViewBag.Errors = TempData["Errors"].ToString(); }

            return View();
        }

        [HttpGet]
        public ActionResult EditMenu(string category)
        {
            if (category != null)
            {
                try
                {
                    if (!category.Equals("DailySpecial"))
                    {
                        int catId = applicationDbContext.Caf_FoodCategories.Where(x => x.Category == category.Trim()).First().CategoryId;
                        EditMenuViewModel editMenuView = new EditMenuViewModel
                        {
                            Item = applicationDbContext.Caf_MenuItems.Where(x => x.CategoryId == catId).ToList()
                        };
                        //List<Caf_MenuItemModel> menuItems = applicationDbContext.Caf_MenuItems.Where(x => x.CategoryId == catId).ToList();
                        return View(editMenuView);
                    }
                    else
                    {
                        int catId = applicationDbContext.Caf_FoodCategories.Where(x => x.Category == category.Trim()).First().CategoryId;
                        EditMenuViewModel editMenuView = new EditMenuViewModel
                        {
                            Item = applicationDbContext.Caf_MenuItems.Where(x => x.CategoryId == catId).ToList(),
                            DailySpecial = applicationDbContext.Caf_DailySpecials.ToList()
                        };

                        return View(editMenuView);
                    }
                }
                catch
                {
                    TempData["Errors"] = "Something went wrong. Please try again.";
                    return RedirectToAction("EditIndex");
                }
            }
            else
            {
                TempData["Errors"] = "Something went wrong. Please try again.";
                return RedirectToAction("EditIndex");
            }
        }

        public void ToggleDailySpecial(int id)
        {
            if(id != null)
            {
                try
                {
                    var selectedSpecial = applicationDbContext.Caf_DailySpecials.Where(x => x.MenuID == id).First();
                    if(selectedSpecial == null)
                    {
                        var newSpecial = new Caf_DailySpecials
                        {
                            MenuID = id,
                            Active = true
                        };
                        selectedSpecial = newSpecial;
                        applicationDbContext.Caf_DailySpecials.Add(newSpecial);
                    }
                    bool state = selectedSpecial.Active;
                    if (state)
                    {
                        state = false;
                    }
                    else { state = true; }
                    selectedSpecial.Active = state;
                    applicationDbContext.SaveChanges();
                }
                catch
                {
                    TempData["Errors"] = "Something went wrong with your request. Please try again";
                    RedirectToAction("EditIndex");
                }
            }
        }
    }
}