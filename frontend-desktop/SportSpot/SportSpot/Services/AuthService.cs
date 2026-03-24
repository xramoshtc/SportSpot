using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using SportSpot.Models;
using System.Collections.Specialized;

namespace SportSpot.Services
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe per gestionar l'autenticació de l'usuari, incloent el login i el logout. 
    /// Aquesta classe utilitza HttpClient per fer peticions al backend i processar les respostes. 
    /// El mètode LoginAsync envia les credencials de l'usuari al backend i retorna un objecte LoginResult
    /// amb la informació del resultat de la autenticació. 
    /// El mètode LogoutAsync envia el token de sessió al backend per tancar la sessió de l'usuari. 
    /// Aquesta classe és essencial per mantenir la seguretat i la gestió d'usuaris a l'aplicació.
    /// </summary>
    public class AuthService
    {
        private readonly HttpClient _httpClient;
        
        public AuthService()
        {
            _httpClient = new HttpClient();
            _httpClient.BaseAddress = new Uri("http://10.2.3.145:8080/");
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per autenticar un usuari amb el backend. Envia les credencials de l'usuari (nom d'usuari i contrasenya)
        /// al backend mitjançant una petició POST. 
        /// </summary>
        /// <param name="user"></param>
        /// <param name="password"></param>
        /// <returns>
        /// Retorna un objecte LoginResult que conté informació sobre l'èxit de l'autenticació, 
        /// un missatge associat, el codi de resultat, el rol de l'usuari i un token de sessió
        /// </returns>
        public async Task<LoginResult> LoginAsync(string user, string password)
        {
            // Creació d'objecte que enviarem al backend
            var loginData = new
            {
                user = user,
                password = password
            };

            // Convertim l'objecte a JSON
            string json = JsonSerializer.Serialize(loginData);

            // Creació del contingut de la petició HTTP
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            // Fer la crida POST al backend
            var response = await _httpClient.PostAsync("api/login", content);

            // Comprovar si la resposta és correcta
            if(!response.IsSuccessStatusCode)
            {
                string responseTextError = await response.Content.ReadAsStringAsync();
                MessageBox.Show("Error de connexió: " + responseTextError);
                return new LoginResult { success = false, message = "Error de connexió" };
            }

            // Llegir la resposta com a text
            string responseText = await response.Content.ReadAsStringAsync();
            
            // Parsejar JSON
            var loginResponse = JsonSerializer.Deserialize<LoginResponse>(responseText);

            // Crear i retornar el resultat
            return new LoginResult
            {
                success = loginResponse.success,
                message = loginResponse.message,
                resultCode = loginResponse.resultCode.ToString(),
                sessionToken = loginResponse.sessionToken,
                role = loginResponse.role
            };
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per tancar la sessió de l'usuari. Envia el token de sessió al backend mitjançant una petició POST
        /// per invalidar-lo i tancar la sessió.
        /// </summary>
        /// <param name="sessionToken"></param>
        /// <returns>
        /// Una cadena de text que conté la resposta del backend, que pot incloure un missatge
        /// de confirmació o error.
        /// </returns>
        public async Task<string> LogoutAsync(string sessionToken)
        {
            string url = "api/logout";

            var json = $"{{ \"token\": \"{sessionToken}\" }}";

            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var response = await _httpClient.PostAsync(url, content);

            string result = await response.Content.ReadAsStringAsync();

            return result;
        }
    }
}
