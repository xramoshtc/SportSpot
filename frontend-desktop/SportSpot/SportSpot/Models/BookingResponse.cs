using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    public class BookingResponse
    {
        public long id { get; set; }
        public long courtId { get; set; }
        public string dateTime { get; set; }
        public int durationMinutes { get; set; }
    }
}
