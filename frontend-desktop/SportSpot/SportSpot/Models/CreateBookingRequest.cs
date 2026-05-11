using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe de request per a crear una reserva, que conté la informació necessària per crear una nova reserva a la base de dades.
    /// </summary>
    public class CreateBookingRequest
    {
        public long courtId { get; set; }
        public string dateTime { get; set; }
        public int durationMinutes { get; set; }
    }
}
