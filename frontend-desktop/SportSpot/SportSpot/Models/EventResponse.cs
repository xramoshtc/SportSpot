using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe (utilitzada per CreateEvent i GetEvents) que representa la resposta d'una consulta d'esdeveniments, incloent informació com el nom 
    /// de la pista, l'organitzador, la data i hora, el nombre de participants actuals i la capacitat 
    /// màxima, així com els noms dels participants. Aquesta classe s'utilitza per retornar informació 
    /// detallada sobre els esdeveniments als clients que realitzen consultes a l'API. 
    /// </summary>
    public class EventResponse
    {
        public int id { get; set; }
        public string title { get; set; }
        public string courtName { get; set; }
        public string organizerName { get; set; }
        public DateTime dateTime { get; set; }
        public int currentParticipants { get; set; }
        public int maxCapacity { get; set; }
        public List<string> participantNames { get; set; }
    }
}
