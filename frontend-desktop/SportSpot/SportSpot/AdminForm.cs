using SportSpot.Models;
using SportSpot.Services;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Http.Json;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SportSpot
{
    public partial class AdminForm : Form
    {
        private readonly AuthService authService = new AuthService();

        public AdminForm()
        {
            InitializeComponent();

            // Verificació de permisos per accedir al formulari
            if (Session.role != "ADMIN")
            {
                MessageBox.Show("No tens persmisos per accedir a aquest formulari.");
                this.Close();
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per tancar sessió quan es tanqui el formulari d'administració. Es crida a AdminForm_FormClosed,
        /// que és l'esdeveniment associat al tancament del formulari.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private async void AdminForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (Session.sessionToken != null)
            {
                try
                {
                    string result = await authService.LogoutAsync(Session.sessionToken);

                    // Convertir el JSON a un object LogoutResponse
                    LogoutResponse logoutResponse = JsonSerializer.Deserialize<LogoutResponse>(result);

                    MessageBox.Show(logoutResponse.message, "Logout", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error en tancar sessió: " + ex.Message);    
                }
            }

            Session.sessionToken = null;
            Session.role = null;
            Session.user = null;

            var login = new LoginForm();
            login.Show();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Aquest mètode només crida a AdminForm_FsormClosed quan es fa clic, 
        /// per assegurar que es netegi la sessió i es torni al LoginForm.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btnLogout_Click(object sender, EventArgs e)
        {
            // Amb això ja cridarem AdminForm_FormClosed per netejar Session i obrir el LoginForm (main)
            this.Close();
        }
    }
}
