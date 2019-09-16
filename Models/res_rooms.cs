using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace BatemanCafeteria.Models
{
    public class res_rooms
    {
        [Key]
        public int room_id { get; set; }
        [DisplayName("Room name")]
        public string room_name { get; set; }
        [DisplayName("Location")]
        public string location { get; set; }
    }
}