using SportSpot.Controls;
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

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per mostrar el control d'usuaris (UCUsuaris) quan es fa clic al botó "Usuaris". Es neteja el panell
        /// de contingut i s'afegeix el control d'usuaris, que es mostrarà a la part central del formulari d'administració.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btnUsers_Click(object sender, EventArgs e)
        {
            var uc = new UCUsuaris();
            uc.Dock = DockStyle.Fill;
            pnlContent.Controls.Clear();
            pnlContent.Controls.Add(uc);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per mostrar el control de perfil privat (UCPrivate) quan es fa clic al botó "Perfil". Es neteja el panell 
        /// de contingut i s'afegeix el control de perfil privat, que es mostrarà a la part central del formulari d'administració, 
        /// permetent a l'usuari veure i editar la seva informació personal.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btnPrivate_Click(object sender, EventArgs e)
        {
            var uc = new UCPrivate();
            uc.Dock = DockStyle.Fill;
            pnlContent.Controls.Clear();
            pnlContent.Controls.Add(uc);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per mostrar el control de pàgina d'inici (UCHomeAdmin) quan es fa clic al botó "Inici". 
        /// Es neteja el panell de contingut i s'afegeix el control de pàgina d'inici, que es mostrarà a la
        /// part central del formulari d'administració, proporcionant una visió general de les 
        /// funcionalitats disponibles i informació rellevant per a l'administrador.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment.</param>
        /// <param name="e">Les dades de l'esdeveniment.</param>
        private void btnHome_Click(object sender, EventArgs e)
        {
            var uc = new UCHomeAdmin();
            uc.Dock = DockStyle.Fill;
            pnlContent.Controls.Clear();
            pnlContent.Controls.Add(uc);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per mostrar el control de gestió de pistes (UCGestCourts) quan es fa clic al botó "Pistes". 
        /// Es neteja el panell de contingut i s'afegeix el control de gestió de pistes, que es mostrarà a la 
        /// part central del formulari d'administració, permetent a l'administrador gestionar les pistes 
        /// disponibles, com ara afegir, editar o eliminar pistes, i visualitzar informació detallada sobre cada pista. 
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment.</param>
        /// <param name="e">Les dades de l'esdeveniment</param>
        private void btnCourts_Click(object sender, EventArgs e)
        {
            var uc = new UCGestCourts();
            uc.Dock = DockStyle.Fill;
            pnlContent.Controls.Clear();
            pnlContent.Controls.Add(uc);

        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per mostrar el control de reserves (UCReserves) quan es fa clic al botó "Reserves". 
        /// Es neteja el panell de contingut i s'afegeix el control de reserves, que es mostrarà a la 
        /// part central del formulari d'administració, permetent a l'administrador gestionar les reserves 
        /// realitzades pels usuaris, com ara visualitzar les reserves pendents, aprovar o rebutjar reserves,
        /// i obtenir informació detallada sobre cada reserva, com ara la data, hora, pista reservada i 
        /// usuari que ha realitzat la reserva. 
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Les dades de l'esdeveniment</param>
        private void btnBook_Click(object sender, EventArgs e)
        {
            var uc = new UCReserves();
            uc.Dock = DockStyle.Fill;
            pnlContent.Controls.Clear();
            pnlContent.Controls.Add(uc);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per mostrar el control de pàgina d'inici (UCHomeAdmin) quan es carrega el formulari
        /// d'administració. Es neteja el panell de contingut i s'afegeix el control de pàgina d'inici, 
        /// que es mostrarà a la part central del formulari d'administració, proporcionant una visió 
        /// general de les funcionalitats disponibles i informació rellevant per a l'administrador des 
        /// del moment en què accedeix al formulari.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Les dades de l'esdeveniment</param>
        private void AdminForm_Load(object sender, EventArgs e)
        {
            pnlContent.Controls.Clear();
            pnlContent.Controls.Add(new UCHomeAdmin() { Dock = DockStyle.Fill });
        }
    }
}