using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe de request per a actualitzar una reserva, que conté la informació necessària per actualitzar una reserva existent a la base de dades. Aquesta classe és utilitzada com a model de dades per enviar la informació necessària al servidor quan es vol actualitzar una reserva a través de l'API. Conté les propietats mínimes requerides per a l'actualització d'una reserva, permetent als clients proporcionar els detalls essencials per a modificar una reserva existent.
    /// </summary>
    public class UpdateBookingRequest
    {
        public string dateTime { get; set; }
        public int durationMinutes { get; set; }
    }
}
