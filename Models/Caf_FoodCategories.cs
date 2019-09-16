using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class Caf_FoodCategories
    {
        [Key]
        public int CategoryId { get; set; }
        public string Category { get; set; }
    }
}