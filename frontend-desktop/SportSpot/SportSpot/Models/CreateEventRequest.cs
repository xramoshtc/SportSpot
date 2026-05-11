using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe per enviar petició a CreateEvent, on només necessitem el títol de l'esdeveniment, la pista 
    /// on es farà i la data i hora. Aquesta classe és utilitzada com a model de dades per enviar la 
    /// informació necessària al servidor quan es vol crear un nou esdeveniment a través de l'API. 
    /// Conté les propietats mínimes requerides per a la creació d'un esdeveniment, permetent als clients 
    /// proporcionar els detalls essencials per a la configuració de l'esdeveniment.
    /// </summary>
    public class CreateEventRequest
    {
        public string title { get; set; }
        public int courtId { get; set; }
        public DateTime dateTime { get; set; }
    }

}
