using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class MenuItems
    {
        ApplicationDbContext applicationDbContext = new ApplicationDbContext();
        public List<Caf_MenuItemModel> GetMenuItems(int category)
        {

            return applicationDbContext.Caf_MenuItems.Where(c => c.CategoryId == category).ToList();
        }
    }
}