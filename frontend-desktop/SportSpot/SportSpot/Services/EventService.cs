using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace SportSpot.Services
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe que encapsula la lògica de comunicació amb l'API d'esdeveniments, proporcionant mètodes 
    /// per crear, obtenir, modificar, unir-se i esborrar esdeveniments. Aquesta classe utilitza HttpClient
    /// per enviar sol·licituds HTTPS a l'API i maneja les respostes, incloent la gestió d'errors i la 
    /// deserialització de les respostes JSON en objectes C#. Això permet als clients interactuar amb 
    /// l'API d'esdeveniments de manera senzilla i estructurada, facilitant la integració de les 
    /// funcionalitats d'esdeveniments en l'aplicació SportSpot. 
    /// </summary>
    public class EventService
    {
        private readonly HttpClient _client;

        public EventService()
        {
            _client = Session.Client;
        }

        public EventService(HttpClient client)
        {
            _client = client;
        }

        private void EnsureBase()
        {
            if (_client.BaseAddress == null)
                _client.BaseAddress = new Uri("https://fakeapi.test/");
        }

        private void AddToken()
        {
            _client.DefaultRequestHeaders.Remove("Session-Token");
            if (!string.IsNullOrEmpty(Session.sessionToken))
                _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per crear un nou esdeveniment enviant una sol·licitud POST a l'API. El mètode accepta un 
        /// objecte CreateEventRequest que conté les dades necessàries per crear l'esdeveniment, com el títol, 
        /// la pista i la data/hora. El mètode serialitza aquest objecte a JSON i l'envia al endpoint "api/events". 
        /// Després de rebre la resposta, el mètode maneja diferents codis d'estat HTTP per determinar si la 
        /// creació va ser exitosa o si hi va haver errors, com permisos insuficients, format incorrecte o token 
        /// invàlid. Si la creació és exitosa, el mètode deserialitza la resposta JSON en un objecte EventResponse i 
        /// el retorna al client.
        /// </summary>
        /// <param name="req">Objecte que conté les dades necessàries per crear l'esdeveniment.</param>
        /// <returns>Objecte EventResponse que representa l'esdeveniment creat.</returns>
        /// <exception cref="Exception">Error en la creació de l'esdeveniment.</exception>
        public async Task<EventResponse> CreateEvent(CreateEventRequest req)
        {
            EnsureBase();
            AddToken();

            var json = JsonSerializer.Serialize(req);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var resposta = await _client.PostAsync("api/events", content);

            if (resposta.StatusCode == HttpStatusCode.Created)
            {
                var jsonResp = await resposta.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<EventResponse>(jsonResp);
            }

            if (resposta.StatusCode == HttpStatusCode.NotFound)
                throw new Exception("La pista no existeix o no tens permisos.");

            if (resposta.StatusCode == HttpStatusCode.BadRequest)
                throw new Exception("Format incorrecte o títol buit.");

            if (resposta.StatusCode == HttpStatusCode.Unauthorized)
                throw new Exception("Token invàlid o expirat.");

            throw new Exception("Error inesperat: " + resposta.StatusCode);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per obtenir la llista d'esdeveniments disponibles enviant una sol·licitud GET a l'API. El 
        /// mètode envia una sol·licitud al endpoint "api/events" i espera rebre una resposta amb un codi d'estat 
        /// HTTP 200 OK.
        /// </summary>
        /// <returns>Llista d'objectes EventResponse que representen els esdeveniments disponibles.</returns>
        public async Task<List<EventResponse>> GetEvents()
        {
            EnsureBase();
            AddToken();

            var resposta = await _client.GetAsync("api/events");
            resposta.EnsureSuccessStatusCode();

            var json = await resposta.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<List<EventResponse>>(json);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per actualitzar un esdeveniment existent enviant una sol·licitud PUT a l'API. El mètode envia 
        /// les dades actualitzades al endpoint "api/events/{id}" i espera rebre una resposta amb un codi d'estat 
        /// HTTP 200 OK.
        /// </summary>
        /// <param name="id">Identificador de l'esdeveniment a actualitzar.</param>
        /// <param name="updatedFields">Objecte que conté els camps actualitzats de l'esdeveniment.</param>
        /// <returns>Objecte EventFullResponse que representa l'esdeveniment actualitzat.</returns>
        /// <exception cref="Exception">Error en l'actualització de l'esdeveniment.</exception>
        public async Task<EventFullResponse> UpdateEvent(int id, object updatedFields)
        {
            EnsureBase();
            AddToken();

            var json = JsonSerializer.Serialize(updatedFields);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var resposta = await _client.PutAsync($"api/events/{id}", content);

            if (resposta.StatusCode == HttpStatusCode.OK)
            {
                var jsonResp = await resposta.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<EventFullResponse>(jsonResp);
            }

            if (resposta.StatusCode == HttpStatusCode.Forbidden)
                throw new Exception("No tens permisos per modificar aquest esdeveniment.");

            if (resposta.StatusCode == HttpStatusCode.NotFound)
                throw new Exception("L'esdeveniment o la pista no existeixen.");

            if (resposta.StatusCode == HttpStatusCode.Conflict)
                throw new Exception("La nova pista no té capacitat suficient.");

            throw new Exception("Error inesperat: " + resposta.StatusCode);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per unir-se a un esdeveniment enviant una sol·licitud POST a l'API. El mètode envia la sol·licitud
        /// al endpoint "api/events/{id}/join" i espera rebre una resposta amb un codi d'estat HTTP 200 OK.
        /// </summary>
        /// <param name="id">Identificador de l'esdeveniment al qual unir-se.</param>
        /// <returns>Objecte EventResponse que representa l'esdeveniment al qual s'ha unit l'usuari.</returns>
        /// <exception cref="Exception">Error en unir-se a l'esdeveniment.</exception>
        public async Task<EventResponse?> JoinEvent(int id)
        {
            EnsureBase();
            AddToken();

            var resposta = await _client.PostAsync($"api/events/{id}/join", null);

            if (resposta.StatusCode == HttpStatusCode.OK)
            {
                var jsonResp = await resposta.Content.ReadAsStringAsync();

                // Si el servidor no retorna JSON, simplement retorna null
                if (string.IsNullOrWhiteSpace(jsonResp))
                    return null;

                return JsonSerializer.Deserialize<EventResponse>(jsonResp);
            }

            if (resposta.StatusCode == HttpStatusCode.NotFound)
                throw new Exception("L'esdeveniment no existeix.");

            if (resposta.StatusCode == HttpStatusCode.Conflict)
                throw new Exception("Ja estàs apuntat o l'esdeveniment està ple.");

            throw new Exception($"Error inesperat: {resposta.StatusCode}");
        }



        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per esborrar un esdeveniment enviant una sol·licitud DELETE a l'API. El mètode envia la sol·licitud
        /// al endpoint "api/events/{id}" i espera rebre una resposta amb un codi d'estat HTTP 204 No Content.
        /// </summary>
        /// <param name="id">Identificador de l'esdeveniment a esborrar.</param>
        /// <returns>True si l'esdeveniment s'ha esborrat correctament, False en cas contrari.</returns>
        /// <exception cref="Exception">Error en esborrar l'esdeveniment.</exception>
        public async Task<bool> DeleteEvent(int id)
        {
            EnsureBase();
            AddToken();

            var resposta = await _client.DeleteAsync($"api/events/{id}");

            if (resposta.StatusCode == HttpStatusCode.NoContent)
                return true;

            if (resposta.StatusCode == HttpStatusCode.Unauthorized)
                throw new Exception("Token invàlid.");

            throw new Exception("Error inesperat: " + resposta.StatusCode);
        }
    }
}
