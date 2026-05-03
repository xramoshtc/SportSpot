using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Json;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Services
{
    using System.Collections.Generic;
    using System.Net;
    using System.Net.Http;
    using System.Net.Http.Json;
    using System.Text.Json;
    using System.Text.Json.Serialization;
    using System.Threading.Tasks;

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Classe per gestionar les operacions relacionades amb les pistes a través de l'API. Aquesta classe 
        /// utilitza HttpClient per realitzar peticions HTTPS a l'endpoint "/api/courts" i inclou el token de 
        /// sessió a la capçalera "Session-Token" per autenticar les peticions. Les operacions inclouen obtenir
        /// la llista de pistes, crear una nova pista, actualitzar les dades d'una pista i eliminar una pista. 
        /// Si alguna operació falla, es llençarà una excepció amb un missatge d'error adequat.
        /// </summary>
        public class PistaService
        {
            private readonly HttpClient _client;

            public PistaService()
            {
                _client = Session.Client;

                // Afegim el token de sessió a totes les peticions
                _client.DefaultRequestHeaders.Clear();
                _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);
            }

            /// Constructor que permet injectar un HttpClient personalitzat, per a proves unitàries.
            public PistaService(HttpClient client)
            {
                _client = client;
            }


            /// <summary>
            /// Autor: Miquel Uribe Faixedas
            /// Mètode per obtenir la llista de totes les pistes disponibles. Aquest mètode realitza una petició GET
            /// a l'endpoint "/api/courts" i retorna una llista d'objectes Pista. Si la petició és exitosa, es 
            /// deserialitza la resposta JSON en una llista de Pista i es retorna. Si hi ha algun error en la petició,
            /// es llençarà una excepció amb el missatge corresponent.
            /// </summary>
            /// <returns>Llista de pistes disponibles.</returns>
            public async Task<List<Pista>> GetPistesAsync()
            {
                var response = await _client.GetAsync("/api/courts");
                response.EnsureSuccessStatusCode();

                return await response.Content.ReadFromJsonAsync<List<Pista>>();
            }

            /// <summary>
            /// Autor: Miquel Uribe Faixedas
            /// Mètode per crear una nova pista. L'usuari ha de tenir permisos d'administrador per poder crear pistes.
            /// Si no es compleixen aquestes condicions, es llençarà una excepció amb el missatge corresponent.
            /// </summary>
            /// <param name="pista">Objecte Pista que conté la informació de la nova pista a crear.</param>
            /// <returns>Retorna l'objecte Pista creat.</returns>
            /// <exception cref="Exception">Llança una excepció si l'usuari no té permisos o si ja existeix una pista amb el mateix nom.</exception>
            public async Task<Pista> CreatePista(string name, string type, decimal pricePerHour, string location)
            {
                Session.Client.DefaultRequestHeaders.Remove("Session-Token");
                Session.Client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

                var json = $@"
                    {{
                        ""name"": ""{name}"",
                        ""type"": ""{type}"",
                        ""pricePerHour"": {pricePerHour.ToString().Replace(",", ".")},
                        ""location"": ""{location}""
                    }}";

                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await _client.PostAsync("api/courts", content);

                if (response.StatusCode == HttpStatusCode.Forbidden)
                    throw new Exception("No tens permisos per crear pistes.");

                if (response.StatusCode == HttpStatusCode.Conflict)
                    throw new Exception("Ja existeix una pista amb aquest nom.");

                response.EnsureSuccessStatusCode();

                var responseJson = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Pista>(responseJson);
            }

            /// <summary>
            /// Autor: Miquel Uribe Faixedas
            /// Mètode per eliminar una pista existent. L'usuari ha de tenir permisos d'administrador per poder
            /// eliminar pistes.
            /// </summary>
            /// <param name="id">ID de la pista a eliminar.</param>
            /// <returns>No retorna cap valor.</returns>
            /// <exception cref="Exception">Llança una excepció si no es poden eliminar la pista.</exception>
            public async Task DeletePista(int id)
            {
                // Afegir el token
                Session.Client.DefaultRequestHeaders.Remove("Session-Token");
                Session.Client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

                // Crida DELETE sense body
                var response = await _client.DeleteAsync($"api/courts/{id}");

                if (response.StatusCode == HttpStatusCode.Forbidden)
                    throw new Exception("No tens permisos per eliminar pistes.");

                if (response.StatusCode == HttpStatusCode.NotFound)
                    throw new Exception("La pista no existeix.");

                // 204 No Content → correcte
                if (response.StatusCode != HttpStatusCode.NoContent)
                    throw new Exception("Error eliminant la pista.");
            }

            /// <summary>
            /// Autor: Miquel Uribe Faixedas
            /// Mètode per modificar una pista existent. L'usuari ha de tenir permisos d'administrador per poder 
            /// modificar pistes.
            /// </summary>
            /// <param name="pista">Objecte Pista amb les dades a modificar.</param>
            /// <returns>Objecte Pista modificat.</returns>
            /// <exception cref="Exception">Llança una excepció si no es poden modificar la pista.  </exception>
            public async Task<Pista> UpdatePista(Pista pista)
            {
                Session.Client.DefaultRequestHeaders.Remove("Session-Token");
                Session.Client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

                var json = $@"
                    {{
                        ""name"": ""{pista.name}"",
                        ""type"": ""{pista.type}"",
                        ""pricePerHour"": {pista.pricePerHour.ToString().Replace(",", ".")},
                        ""capacity"": {pista.capacity},
                        ""location"": ""{pista.location}""
                    }}";

                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await _client.PutAsync($"api/courts/{pista.id}", content);

                if (response.StatusCode == HttpStatusCode.Forbidden)
                    throw new Exception("No tens permisos per modificar pistes.");

                response.EnsureSuccessStatusCode();

                var responseJson = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Pista>(responseJson);
            }
        }
    }
