using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe per llegir la resposta del backend quan es fa logout, que conté un codi de resultat, 
    /// un missatge i una ruta (path) a la qual redirigir-se si el logout és correcte (path pendent...).
    /// </summary>
    public class LogoutResponse
    {
        public int resultCode { get; set; }
        public string message { get; set; }
        public string path { get; set; }    
    }
}
