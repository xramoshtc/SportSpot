using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe de resposta per a les reserves, que conté la informació necessària per mostrar les reserves a l'administrador.
    /// </summary>
    public class BookingResponse
    {
        public long id { get; set; }
        public long courtId { get; set; }
        public string dateTime { get; set; }
        public int durationMinutes { get; set; }
    }
}
