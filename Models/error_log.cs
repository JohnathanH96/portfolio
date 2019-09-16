using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class error_log
    {
        [Key]
        public int log_id { get; set; }

        public string app_name { get; set; }

        public string controller { get; set; }

        public string method_name { get; set; }

        public string error_mes { get; set; }

        public DateTime audit_create_dt { get; set; }

    }
}