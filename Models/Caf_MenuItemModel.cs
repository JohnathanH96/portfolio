using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class Caf_MenuItemModel
    {
        [Key]
        public int MenuID { get; set; }
        [Required(ErrorMessage = "Title is required")]
        [StringLength(450)]
        [Index(IsUnique = true)]
        public string Title { get; set; }
        [Required(ErrorMessage = "Price is required")]
        [RegularExpression(@"^\d+\.\d{0,2}$", ErrorMessage = "Entry must be in form 0.00")]
        [Range(0, 9999999999999999.99, ErrorMessage = "Price must be positive")]
        public decimal Price { get; set; }
        [Required(ErrorMessage = "Description is required")]
        public string Description { get; set; }
        [Range(0, int.MaxValue, ErrorMessage = "Calories cannot be negative")]
        public int Calories { get; set; }
        [DisplayName("Upload Image")]
        public string ImgLocation { get; set; }
        [DisplayName("Category")]
        [Required(ErrorMessage = "Category is required")]
        public int CategoryId { get; set; }
        public virtual Caf_FoodCategories FoodCategory { get; set; }

        [NotMapped]
        public HttpPostedFileBase ImageFile { get; set; }
    }
}