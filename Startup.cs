using Microsoft.Owin;
using Owin;
using BatemanCafeteria.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;

[assembly: OwinStartupAttribute(typeof(BatemanCafeteria.Startup))]
namespace BatemanCafeteria
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
            CreateRolesAndUsers();
        }
        private void CreateRolesAndUsers()
        {
            ApplicationDbContext applicationDbContext = new ApplicationDbContext();
            var roleManager = new RoleManager<IdentityRole>(new RoleStore<IdentityRole>(applicationDbContext));
            var userManager = new UserManager<ApplicationUser>(new UserStore<ApplicationUser>(applicationDbContext));

            if (!roleManager.RoleExists("Caf_Admin"))
            {
                var role = new IdentityRole();
                role.Name = "Caf_Admin";
                roleManager.Create(role);

                var user = new ApplicationUser();
                user.UserName = "testAdmin@outlook.com";
                user.Email = "testAdmin@outlook.com";
                string userPWD = "bZ^7xm&A";
                var chkUser = userManager.Create(user, userPWD);
                if (chkUser.Succeeded)
                {
                    var result = userManager.AddToRole(user.Id, "Caf_Admin");
                }


            }
            if (!roleManager.RoleExists("Caf_Secretary"))
            {
                var role = new IdentityRole();
                role.Name = "Caf_Secretary";
                roleManager.Create(role);

                var user = new ApplicationUser();
                user.UserName = "testSecretary@outlook.com";
                user.Email = "testSecretary@outlook.com";
                string userPWD = "password";
                var chkUser = userManager.Create(user, userPWD);
                if (chkUser.Succeeded)
                {
                    var result = userManager.AddToRole(user.Id, "Caf_Secretary");
                }
            }
        }
    }
}
