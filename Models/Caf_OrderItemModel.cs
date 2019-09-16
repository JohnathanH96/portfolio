using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class Caf_OrderItemModel
    {
        [Key]
        public int OrderItemID { get; set; }
        public int InvoiceID { get; set; }
        public int Quantity { get; set; }
        public decimal UnitPrice { get; set; }
        public string Special_instructions { get; set; }
        public int MenuID { get; set; }
        public virtual Caf_MenuItemModel MenuItem { get; set; }
        public virtual Caf_InvoiceModel Invoice { get; set; }
    }
}