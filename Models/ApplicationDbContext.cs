using Microsoft.AspNet.Identity.EntityFramework;
using MySql.Data.Entity;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    [DbConfigurationType(typeof(MySqlEFConfiguration))]
    public class ApplicationDbContext : IdentityDbContext<ApplicationUser>
    {
        public ApplicationDbContext()
            : base("DefaultConnection", throwIfV1Schema: false)
        {
            this.Configuration.LazyLoadingEnabled = true;
            this.Configuration.ProxyCreationEnabled = true;
        }

        #region tables
        public virtual DbSet<Caf_InvoiceModel> Caf_Invoices { get; set; }
        public virtual DbSet<Caf_CartModel> Caf_Carts { get; set; }
        public virtual DbSet<Caf_OrderItemModel> Caf_OrderItems { get; set; }
        public virtual DbSet<Caf_MenuItemModel> Caf_MenuItems { get; set; }
        public virtual DbSet<Caf_FoodStatusModel> Caf_FoodStatuses { get; set; }
        public virtual DbSet<Caf_FoodCategories> Caf_FoodCategories { get; set; }
        public virtual DbSet<error_log> Error_Logs { get; set; }
        public virtual DbSet<res_reservations> res_reservations { get; set; }
        public virtual DbSet<res_rooms> res_rooms { get; set; }
        public virtual DbSet<Caf_Caterings> Caf_Caterings { get; set; }
        public virtual DbSet<Caf_DailySpecials> Caf_DailySpecials { get; set; }
        public virtual DbSet<Caf_ServiceUsers> Caf_ServiceUsers { get; set; }
        #endregion tables

        public static ApplicationDbContext Create()
        {
            return new ApplicationDbContext();
        }
    }
}