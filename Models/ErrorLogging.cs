using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class ErrorLogging
    {
        public static ApplicationDbContext db = new ApplicationDbContext();

        /// <summary>
        /// Function will insert an error log into the database when something breaks within one of
        /// the applications, this class is shared amongst the 3 projects.
        /// This provides an easier way of debugging in the case of something breaking.
        /// </summary>
        /// <param name="app_name"></param>
        /// <param name="controller"></param>
        /// <param name="method_name"></param>
        /// <param name="error_mes"></param>
        public void log_error(string app_name, string controller, string method_name, string error_mes)
        {
            DateTime currentTime = DateTime.Now;
            Convert.ToDateTime(currentTime.ToString("yyyy-MM-dd H:mm:ss"));
            error_log log = new error_log
            {
                app_name = app_name,
                controller = controller,
                method_name = method_name,
                error_mes = error_mes,
                audit_create_dt = Convert.ToDateTime(currentTime.ToString("yyyy-MM-dd H:mm:ss"))
            };
            db.Error_Logs.Add(log);
            db.SaveChanges();
        }
    }
}