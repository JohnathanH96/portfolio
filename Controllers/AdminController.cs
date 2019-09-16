using BatemanCafeteria.Models;
using BatemanCafeteria.ViewModels;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace BatemanCafeteria.Controllers
{
    [Authorize(Roles = "Caf_Admin")]
    public class AdminController : Controller
    {
        ApplicationDbContext applicationDbContext = new ApplicationDbContext();
        // GET: Admin
        public ActionResult Index()
        {
            return View();
        }

        [HttpGet]
        public ActionResult ManageUsers()
        {
            if(TempData["Errors"] != null)
            {
                ViewBag.Errors = TempData["Errors"].ToString();
            }
            List<ApplicationUser> users = applicationDbContext.Users.Where(x => x.Roles.Any(r => r.RoleId == applicationDbContext.Roles.Where(y => y.Name == "Caf_Secretary").FirstOrDefault().Id)).ToList();
            return View(users);
        }

        [HttpGet]
        public ActionResult AddUser()
        {
            return RedirectToAction("Register", "Account");
        }
        
        [HttpGet]
        public ActionResult DeleteUser(string id)
        {
            ApplicationUser user = applicationDbContext.Users.Find(id);
            if (user != null)
            {
                return PartialView("_DeleteUser", user);
            }
            else
            {
                TempData["Errors"] = "Something went wrong. Could not find user. Please try again.";
                return RedirectToAction("ManageUsers");
            }
        }

        [HttpPost]
        public ActionResult DeleteUser(ApplicationUser user)
        {
            applicationDbContext.Users.Attach(user);
            var userManager = new UserManager<ApplicationUser>(new UserStore<ApplicationUser>(applicationDbContext));
            var result = userManager.Delete(user);
            if (result.Succeeded)
            {
                return RedirectToAction("ManageUsers", "Admin");
            }
            else
            {
                TempData["Errors"] = "Error in deleting user. Please try again.";
                return RedirectToAction("ManageUsers", "Admin");
            }
        }


        [HttpGet]
        public ActionResult ManageImages()
        {
            if (TempData["Errors"] != null)
            {
                ViewBag.Errors = TempData["Errors"].ToString();
            }
            string[] fileNames = Directory.GetFiles(Server.MapPath("/Images/MenuItems"));
            var items = applicationDbContext.Caf_MenuItems.ToList();
            List<ImageViewModel> images = new List<ImageViewModel>();
            foreach(var file in fileNames)
            {
                string path = "/Images/MenuItems/" + Path.GetFileName(file);
                bool result = applicationDbContext.Caf_MenuItems.Where(x => x.ImgLocation == path).Any();
                ImageViewModel image = new ImageViewModel
                {
                    ImagePath = path,
                    ImageName = Path.GetFileNameWithoutExtension(file),
                    IsBeingUsed = result
                };
                images.Add(image);
            }
            return View(images);
        }

        [HttpGet]
        public ActionResult DeleteImage(string imagePath)
        {
            ImageViewModel image = new ImageViewModel
            {
                ImageName = Path.GetFileNameWithoutExtension(imagePath),
                ImagePath = imagePath
            };
            return PartialView(image);
        }

        [HttpPost]
        public ActionResult DeleteImage(ImageViewModel image)
        {
            var items = applicationDbContext.Caf_MenuItems.ToList();
            bool result = applicationDbContext.Caf_MenuItems.Where(x => x.ImgLocation == image.ImagePath).Any();
            if (!result)
            {
                try
                {
                    System.IO.File.Delete(Server.MapPath(image.ImagePath));
                    return RedirectToAction("ManageImages");
                }
                catch
                {
                    TempData["Errors"] = "Could not delete image. Please try again.";
                    return RedirectToAction("ManageImages");
                }
            }
            else
            {
                TempData["Errors"] = "Could not delete image. Selected image is in use by a menu item.";
                return RedirectToAction("ManageImages");
            }
        }

        [HttpGet]
        public ActionResult ManageServiceUsers()
        {
            List<Caf_ServiceUsers> users = applicationDbContext.Caf_ServiceUsers.ToList();
            return View(users);
        }

        public ActionResult BanUser(string userName)
        {
            Caf_ServiceUsers user = applicationDbContext.Caf_ServiceUsers.Where(x => x.Username == userName).FirstOrDefault();
            if(user != null)
            {
                if (user.IsBanned)
                {
                    user.IsBanned = false;
                }
                else
                {
                    user.IsBanned = true;
                }
                applicationDbContext.SaveChanges();
            }
            return RedirectToAction("ManageServiceUsers");
        }

    }
}