using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class Caf_DailySpecials
    {
        [Key]
        public int SpecialId { get; set; }
        public int MenuID { get; set; }
        public virtual Caf_MenuItemModel MenuItem { get; set; }
        public bool Active { get; set; }
    }
}