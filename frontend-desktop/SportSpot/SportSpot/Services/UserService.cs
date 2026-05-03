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
    /// Classe per gestionar les operacions relacionades amb els usuaris a través de l'API. Aquesta classe
    /// utilitza HttpClient per realitzar peticions HTTPS a l'endpoint "/api/users" i inclou el token de 
    /// sessió a la capçalera "Session-Token" per autenticar les peticions. Les operacions inclouen obtenir
    /// la llista d'usuaris, obtenir el perfil de l'usuari actual, actualitzar les dades d'un usuari, 
    /// canviar la contrasenya i eliminar un usuari. Si alguna operació falla, es llençarà una excepció 
    /// amb un missatge d'error adequat.
    /// </summary>
    public class UserService
    {
        private readonly HttpClient _client;

        public UserService()
        {
            _client = Session.Client;
        }

        // Constructor que permet injectar un HttpClient personalitzat, per a proves unitàries.
        public UserService(HttpClient client)
        {
            _client = client;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per obtenir la llista de tots els usuaris registrats a través de l'API. El mètode realitza 
        /// una petició GET a l'endpoint "/api/users" i inclou el token de sessió a la capçalera "Session-Token"
        /// per autenticar la petició. Si la resposta és exitosa, es deserialitza el contingut JSON en una llista 
        /// d'objectes Usuari i es retorna aquesta llista. Si la resposta no és exitosa, es llença una 
        /// excepció amb un missatge d'error adequat. Aquest mètode és útil per obtenir informació sobre tots 
        /// els usuaris del sistema, com els seus noms, correus electrònics, rols i estats d'activitat.
        /// </summary>
        /// <returns>Llista d'objectes Usuari.</returns>
        public async Task<List<Usuari>> GetUsers()
        {
            _client.DefaultRequestHeaders.Remove("Session-Token");
            _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

            var resposta = await _client.GetAsync("api/users");

            resposta.EnsureSuccessStatusCode();

            var json = await resposta.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<List<Usuari>>(json);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per obtenir el perfil de l'usuari actual a través de l'API. El mètode realitza una petició 
        /// GET a l'endpoint "/api/users/me" i inclou el token de sessió a la capçalera "Session-Token" per 
        /// autenticar la petició. Si la resposta és exitosa, es deserialitza el contingut JSON en un objecte 
        /// Usuari i es retorna aquest objecte. Si la resposta no és exitosa, es llença una excepció amb un 
        /// missatge d'error adequat.
        /// </summary>
        /// <returns>Objecte Usuari que representa el perfil de l'usuari actual.</returns>
        public async Task<Usuari> GetMyProfile()
        {
            _client.DefaultRequestHeaders.Remove("Session-Token");
            _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

            var resposta = await _client.GetAsync("api/users/me");
            resposta.EnsureSuccessStatusCode();

            var json = await resposta.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<Usuari>(json);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per actualitzar les dades d'un usuari a través de l'API. El mètode realitza una petició PUT 
        /// a l'endpoint "/api/users/{username}" on {username} és el nom d'usuari de l'usuari que es vol modificar. 
        /// La petició inclou el token de sessió a la capçalera "Session-Token" per autenticar la petició i un cos 
        /// JSON que conté les dades modificades de l'usuari. Si la resposta és exitosa, es retorna true. Si la 
        /// resposta no és exitosa, es llença una excepció amb un missatge d'error adequat. Aquest mètode és útil
        /// per permetre als administradors o als propis usuaris modificar les seves dades personals, com el nom,
        /// el correu electrònic o el rol, segons els permisos que tinguin.
        /// </summary>
        /// <param name="username">Nom d'usuari de l'usuari que es vol modificar.</param>
        /// <param name="usuariModificat">Objecte que conté les dades modificades de l'usuari.</param>
        /// <returns>True si l'actualització és exitosa, sinó llença una excepció.</returns>
        /// <exception cref="Exception">Llança una excepció si hi ha un error en l'actualització de l'usuari.</exception>
        public async Task<bool> UpdateUser(string username, object usuariModificat)
        {
            _client.DefaultRequestHeaders.Remove("Session-Token");
            _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

            var json = JsonSerializer.Serialize(usuariModificat);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var resposta = await _client.PutAsync($"api/users/{username}", content);

            if (resposta.IsSuccessStatusCode)
                return true;

            if (resposta.StatusCode == HttpStatusCode.Forbidden)
                throw new Exception("No tens permisos per modificar aquest usuari");

            if (resposta.StatusCode == HttpStatusCode.NotFound)
                throw new Exception("Usuari no trobat");

            throw new Exception("Error actualitzant dades: " + resposta.StatusCode);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per actualitzar la contrasenya d'un usuari a través de l'API. El mètode realitza una 
        /// petició PUT a l'endpoint "/api/users/{username}" on {username} és el nom d'usuari de l'usuari 
        /// que es vol modificar. La petició inclou el token de sessió a la capçalera "Session-Token" per 
        /// autenticar la petició i un cos JSON que conté la nova contrasenya de l'usuari. Si la resposta és exitosa, 
        /// es retorna true. Si la resposta no és exitosa, es llença una excepció amb un missatge d'error adequat.
        /// </summary>
        /// <param name="username">Nom d'usuari de l'usuari que es vol modificar.</param>
        /// <param name="newPassword">Nova contrasenya de l'usuari.</param>
        /// <returns>True si l'actualització és exitosa, sinó llença una excepció.</returns>
        /// <exception cref="Exception">Llança una excepció si hi ha un error en l'actualització de la contrasenya.</exception>
        public async Task<bool> UpdatePassword(string username, string newPassword)
        {
            _client.DefaultRequestHeaders.Remove("Session-Token");
            _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

            var body = new { password = newPassword };
            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var resposta = await _client.PutAsync($"api/users/{username}", content);

            if (resposta.IsSuccessStatusCode)
                return true;

            if (resposta.StatusCode == HttpStatusCode.Forbidden)
                throw new Exception("No tens permisos per modificar aquest usuari");

            if (resposta.StatusCode == HttpStatusCode.NotFound)
                throw new Exception("Usuari no trobat");

            throw new Exception("Error actualitzant contrasenya: " + resposta.StatusCode);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per eliminar un usuari a través de l'API. El mètode realitza una petició DELETE a 
        /// l'endpoint "/api/users/{username}" on {username} és el nom d'usuari de l'usuari que es vol 
        /// eliminar. La petició inclou el token de sessió a la capçalera "Session-Token" per autenticar la petició. 
        /// Si la resposta és exitosa, es retorna true. Si la resposta no és exitosa, es llença una excepció amb un missatge d'error adequat.
        /// </summary>
        /// <param name="username">Nom d'usuari de l'usuari que es vol eliminar.</param>
        /// <returns>True si l'eliminació és exitosa, sinó llença una excepció.</returns>
        /// <exception cref="Exception">Llança una excepció si hi ha un error en l'eliminació de l'usuari.  </exception>
        public async Task<bool> DeleteUser(string username)
        {
            _client.DefaultRequestHeaders.Remove("Session-Token");
            _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

            var resposta = await _client.DeleteAsync($"api/users/{username}");

            if (resposta.StatusCode == HttpStatusCode.NoContent)
                return true;

            if (resposta.StatusCode == HttpStatusCode.Forbidden)
                throw new Exception("No tens permisos per eliminar aquest compte.");

            if (resposta.StatusCode == HttpStatusCode.NotFound)
                throw new Exception("Usuari no trobat.");

            throw new Exception($"Error en eliminar el compte. Codi: {(int)resposta.StatusCode}");
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per crear un nou usuari a través de l'API. El mètode realitza una petició POST a l'endpoint
        /// "/api/users/newuser" i inclou el token de sessió a la capçalera "Session-Token" per autenticar la 
        /// petició. La petició també inclou un cos JSON que conté les dades del nou usuari, com el nom d'usuari,
        /// el correu electrònic, la contrasenya i el rol. Si la resposta és exitosa i el nou usuari es crea 
        /// correctament, es retorna true. Si la resposta no és exitosa, es llença una excepció amb un missatge 
        /// d'error adequat, com ara format incorrecte o nom d'usuari ja existent. Aquest mètode és útil per 
        /// permetre als administradors crear nous comptes d'usuari al sistema. 
        /// </summary>
        /// <param name="nouUsuari">Objecte que conté les dades del nou usuari.</param>
        /// <returns>True si el nou usuari es crea correctament, sinó llença una excepció.</returns>
        /// <exception cref="Exception">Llança una excepció si hi ha un error en la creació del nou usuari.</exception>
        public async Task<bool> CreateNewUser(object nouUsuari)
        {
            _client.DefaultRequestHeaders.Remove("Session-Token");
            _client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

            var json = JsonSerializer.Serialize(nouUsuari);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var resposta = await _client.PostAsync("api/users/newuser", content);

            if (resposta.StatusCode == HttpStatusCode.Created)
                return true;

            if (resposta.StatusCode == HttpStatusCode.BadRequest)
                throw new Exception("Format incorrecte. Revisa les dades.");

            if (resposta.StatusCode == HttpStatusCode.Conflict)
                throw new Exception("Aquest nom d'usuari ja existeix.");

            throw new Exception("Error inesperat: " + resposta.StatusCode);
        }

    }
}
