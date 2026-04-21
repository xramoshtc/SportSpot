using Microsoft.VisualBasic.ApplicationServices;
using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;


namespace SportSpot
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe per mostra un DataGridView amb els usuaris obtinguts de l'API. El token de sessió s'envia a través de la capçalera 
    /// "Session-Token" per autenticar la petició. Si la resposta és correcta, els usuaris es deserialitzen i es mostren 
    /// al DataGridView. Si hi ha un error, es mostra un missatge d'error.
    /// </summary>
    public partial class UCUsuaris : UserControl
    {
        public UCUsuaris()
        {
            InitializeComponent();
            _ = CarregarUsuarisAsync();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Tasca per carregar els usuaris des de l'API. Utilitza HttpClient per fer una petició GET a l'endpoint "/api/users" 
        /// i inclou el token de sessió a la capçalera. Si la resposta és exitosa, deserialitza el JSON rebut en una llista d'usuaris
        /// i la mostra al DataGridView. Si hi ha un error, mostra un missatge d'error.
        /// </summary>
        /// <returns></returns>
        private async Task CarregarUsuarisAsync()
        {
            try
            {
                using (var client = new HttpClient())
                {
                    // Afegim el token a la capçalera
                    client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

                    var resposta = await client.GetAsync("http://10.2.3.145:8080/api/users");

                    if (resposta.IsSuccessStatusCode)
                    {
                        var json = await resposta.Content.ReadAsStringAsync();

                        var usuaris = JsonSerializer.Deserialize<List<Usuari>>(json);

                        dataGridViewUsuaris.DataSource = usuaris;
                    }
                    else
                    {
                        MessageBox.Show("Error carregant usuaris: " + resposta.StatusCode);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de connexió: " + ex.Message);
            }
        }

        private async void btnNewAdmin_Click(object sender, EventArgs e)
        {
            var form = new NewAdminForm();
            form.ShowDialog();
            await CarregarUsuarisAsync();
        }
    }
}
