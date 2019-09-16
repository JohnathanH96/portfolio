using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class Caf_Caterings
    {
        [Key]
        public int CateringId { get; set; }
        public int InvoiceID { get; set; }
        public virtual Caf_InvoiceModel Invoice { get; set; }
        public int res_id { get; set; }
        public virtual res_reservations Reservation { get; set; }
        public DateTime Time { get; set; }
        public string Instructions { get; set; }
    }
}