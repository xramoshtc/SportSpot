using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace SportSpot.Services
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe de servei per a gestionar les reserves, que conté mètodes per crear, obtenir, actualitzar i eliminar reserves a través de l'API. Aquesta classe utilitza HttpClient per enviar peticions HTTP al servidor i processar les respostes. Cada mètode assegura que la base URL estigui configurada i que el token de sessió estigui inclòs a les capçaleres de les peticions. Els mètodes retornen els resultats deserialitzats en objectes de resposta o llistes d'objectes, segons sigui necessari. Aquesta classe és essencial per a la comunicació entre el client i el servidor pel que fa a la gestió de les reserves.
    /// </summary>
    public class BookingService
    {
        private readonly HttpClient _client;

        public BookingService()
        {
            _client = Session.Client;
        }

        private void EnsureBase()
        {
            if (_client.BaseAddress == null)
                _client.BaseAddress = new Uri("https://10.2.3.145:8443/");
        }

        private void AddToken()
        {
            _client.DefaultRequestHeaders.Remove("Session-Token");
            _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per crear una nova reserva. Aquest mètode accepta un objecte CreateBookingRequest que conté els detalls de la reserva a crear, com el ID de la pista, la data i hora de la reserva i la durada en minuts. El mètode serialitza aquest objecte a JSON i l'envia al servidor mitjançant una petició POST a l'endpoint "api/bookings". Si la resposta del servidor és exitosa, el mètode deserialitza la resposta JSON en un objecte BookingResponse i el retorna. Si la resposta no és exitosa, retorna null.
        /// </summary>
        /// <param name="request">Objecte que conté els detalls de la reserva a crear.</param>
        /// <returns>Objecte BookingResponse si la creació és exitosa, sinó null.</returns>        
        public async Task<BookingResponse?> CreateBooking(CreateBookingRequest request)
        {
            EnsureBase();
            AddToken();

            var json = JsonSerializer.Serialize(request);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var resposta = await _client.PostAsync("api/bookings", content);

            if (!resposta.IsSuccessStatusCode)
                return null;

            var jsonResp = await resposta.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<BookingResponse>(jsonResp);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per obtenir les reserves de l'usuari actual. Aquest mètode envia una petició GET a l'endpoint "api/bookings/my" per recuperar les reserves associades a l'usuari autenticat. Si la resposta del servidor és exitosa, el mètode deserialitza la resposta JSON en una llista d'objectes BookingResponse i la retorna. Si la resposta no és exitosa, retorna una llista buida.
        /// </summary>
        /// <returns>Llista d'objectes BookingResponse amb les reserves de l'usuari actual.</returns>
        public async Task<List<BookingResponse>> GetMyBookings()
        {
            EnsureBase();
            AddToken();

            var resposta = await _client.GetAsync("api/bookings/my");

            if (!resposta.IsSuccessStatusCode)
                return new List<BookingResponse>();

            var json = await resposta.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<List<BookingResponse>>(json);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per obtenir les reserves d'una pista específica. Aquest mètode envia una petició GET a l'endpoint "api/courts/{courtId}/bookings" per recuperar les reserves associades a la pista indicada. Si es proporciona una data, només es recuperen les reserves d'aquella data. Si la resposta del servidor és exitosa, el mètode deserialitza la resposta JSON en una llista d'objectes BookingResponse i la retorna. Si la resposta no és exitosa, retorna una llista buida.
        /// </summary>
        /// <param name="courtId">ID de la pista per a la qual es volen obtenir les reserves.</param>
        /// <param name="date">Data específica per filtrar les reserves (opcional).</param>
        /// <returns>Llista d'objectes BookingResponse amb les reserves de la pista especificada.</returns>
        public async Task<List<BookingResponse>> GetBookingsByCourt(int courtId, string? date = null)
        {
            EnsureBase();
            AddToken();

            string url = $"api/courts/{courtId}/bookings";
            if (!string.IsNullOrEmpty(date))
                url += $"?date={date}";

            var resposta = await _client.GetAsync(url);

            if (!resposta.IsSuccessStatusCode)
                return new List<BookingResponse>();

            var json = await resposta.Content.ReadAsStringAsync();
            
            if (string.IsNullOrWhiteSpace(json))
                return new List<BookingResponse>();

            return JsonSerializer.Deserialize<List<BookingResponse>>(json);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per eliminar una reserva específica. Aquest mètode accepta l'ID de la reserva que es vol eliminar i envia una petició DELETE a l'endpoint "api/bookings/{id}". Si la resposta del servidor és exitosa, el mètode retorna true, indicant que la reserva s'ha eliminat correctament. Si la resposta no és exitosa, retorna false.
        /// </summary>
        /// <param name="id">ID de la reserva que es vol eliminar.</param>
        /// <returns>True si la reserva s'ha eliminat correctament, false en cas contrari.</returns>
        public async Task<bool> DeleteBooking(long id)
        {
            EnsureBase();
            AddToken();

            var resposta = await _client.DeleteAsync($"api/bookings/{id}");
            return resposta.IsSuccessStatusCode;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per actualitzar una reserva específica. Aquest mètode accepta l'ID de la pista associada a la reserva que es vol actualitzar i un objecte UpdateBookingRequest que conté els detalls actualitzats de la reserva, com la nova data i hora i la nova durada en minuts. El mètode serialitza aquest objecte a JSON i l'envia al servidor mitjançant una petició PUT a l'endpoint "api/bookings/court/{courtId}". Si la resposta del servidor és exitosa, el mètode deserialitza la resposta JSON en una llista d'objectes BookingResponse (en cas que hi hagi múltiples reserves associades a la pista) i la retorna. Si la resposta no és exitosa, retorna una llista buida.
        /// </summary>
        /// <param name="courtId">ID de la pista associada a la reserva que es vol actualitzar.</param>
        /// <param name="request">Objecte UpdateBookingRequest que conté els detalls actualitzats de la reserva.</param>
        /// <returns>Llista d'objectes BookingResponse amb les reserves actualitzades.</returns>
        public async Task<List<BookingResponse>> UpdateBooking(long courtId, UpdateBookingRequest request)
        {
            EnsureBase();
            AddToken();

            var json = JsonSerializer.Serialize(request);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var resposta = await _client.PutAsync($"api/bookings/court/{courtId}", content);

            if (!resposta.IsSuccessStatusCode)
                return new List<BookingResponse>();

            var jsonResp = await resposta.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<List<BookingResponse>>(jsonResp);
        }
    }

}
