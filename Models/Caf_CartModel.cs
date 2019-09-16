using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class Caf_CartModel
    {
        [Key]
        public int RecordID { get; set; }
        public string CartID { get; set; }
        public int MenuID { get; set; }
        public int Quantity { get; set; }
        public string SpecialInstructions { get; set; }
        public System.DateTime DateCreated { get; set; }
        public virtual Caf_MenuItemModel MenuItem { get; set; }
    }
}