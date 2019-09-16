using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class Caf_FoodStatusModel
    {
        [Key]
        public int StatusId { get; set; }
        public string Status { get; set; }
    }
}