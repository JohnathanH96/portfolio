using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace BatemanCafeteria.Models
{
    public class Caf_InvoiceModel
    {
        [ScaffoldColumn(false)]
        [Key]
        public int InvoiceID { get; set; }
        [DisplayName("Name")]
        [StringLength(160)]
        [Required]
        public string Customer_name { get; set; }
        [DisplayName("Email Address")]
        [RegularExpression(@"[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}",
            ErrorMessage = "Email is is not valid.")]
        [DataType(DataType.EmailAddress)]
        [Required]
        public string Customer_email { get; set; }
        [DisplayName("Phone Number")]
        [StringLength(24)]
        [DataType(DataType.PhoneNumber)]
        [Required]
        public string Customer_phone { get; set; }
        [ScaffoldColumn(false)]
        public string Order_date { get; set; }
        [ScaffoldColumn(false)]
        public string Order_time { get; set; }
        [DisplayName("Total")]
        public decimal Order_total { get; set; }
        [ScaffoldColumn(false)]
        public bool Payment_status { get; set; } = false;
        [ScaffoldColumn(false)]
        public int StatusId { get; set; }
        public virtual Caf_FoodStatusModel FoodStatus { get; set; }
        public List<Caf_OrderItemModel> Order_items { get; set; }

        public string Username { get; set; }
    }
}