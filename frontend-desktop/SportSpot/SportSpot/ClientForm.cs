using SportSpot.Models;
using SportSpot.Services;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SportSpot
{
    public partial class ClientForm : Form
    {
        private readonly AuthService authService = new AuthService();
        public ClientForm()
        {
            InitializeComponent();

            // Verificació de permisos per accedir al formulari de client: Com a ADMIN permetre accedir
            if (Session.role != "CLIENT" && Session.role != "ADMIN")
            {
                MessageBox.Show("No tens permisos per accedir a aquest formulari.");
                this.Close();
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan es tanca el formulari de client. Si hi ha un token de sessió actiu, 
        /// es fa una crida al servei d'autenticació per tancar la sessió. Després, es neteja la informació 
        /// de la sessió i es mostra el formulari de login.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private async void ClientForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            if(Session.sessionToken != null)
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
        /// Mètode que es crida quan es fa clic al botó de logout. Simplement tanca el formulari, 
        /// i això ja desencadenarà el procés de logout definit a ClientForm_FormClosed.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btnLogout_Click(object sender, EventArgs e)
        {
            // Amb això ja cridarem ClientForm_FormClosed per netejar Session i obrir el LoginForm (main)
            this.Close();
        }
    }
}
