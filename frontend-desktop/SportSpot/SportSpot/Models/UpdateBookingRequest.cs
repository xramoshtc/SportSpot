using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    public class UpdateBookingRequest
    {
        public string dateTime { get; set; }
        public int durationMinutes { get; set; }
    }
}
