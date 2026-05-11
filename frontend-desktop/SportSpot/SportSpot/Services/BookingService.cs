using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace SportSpot.Services
{
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

        
        public async Task<bool> DeleteBooking(long id)
        {
            EnsureBase();
            AddToken();

            var resposta = await _client.DeleteAsync($"api/bookings/{id}");
            return resposta.IsSuccessStatusCode;
        }

        
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
