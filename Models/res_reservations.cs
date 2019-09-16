using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class res_reservations
    {
        [Key]
        public int res_id { get; set; }
        public string user_id { get; set; }
        public string email_addr { get; set; }
        public string phone_ext { get; set; }
        public DateTime res_dt { get; set; }
        public DateTime res_start { get; set; }
        public DateTime res_end { get; set; }
        public int room_id { get; set; }
        public string res_name { get; set; }
        public string cat_ind { get; set; }
        public string cat_order { get; set; }
        public int ver_code { get; set; }
        public string pending_ind { get; set; }
        public string approved_ind { get; set; }
        public string reject_ind { get; set; }
        public DateTime audit_create_dt { get; set; }
        public string void_ind { get; set; }
    }
}