using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class Caf_ServiceUsers
    {
        [Key]
        public int ServiceUsersId { get; set; }
        [Required]
        [DisplayName("Username")]
        public string Username { get; set; }
        [Required]
        [DisplayName("Machine Name")]
        public string MachineName { get; set; }
        [Required]
        [DisplayName("Domain Name")]
        public string DomainName { get; set; }
        [Required]
        [DisplayName("Banned")]
        public bool IsBanned { get; set; }
    }
}