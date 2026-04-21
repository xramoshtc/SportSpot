using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SportSpot
{
    public partial class RegisterForm : Form
    {
        public RegisterForm()
        {
            InitializeComponent();
        }

        private void lblTittleLogin_Click(object sender, EventArgs e)
        {

        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mêtode per validar el camp d'usuari quan es deixa de fer focus. Si el camp està buit o té menys de 4 caràcters, 
        /// es mostrarà un missatge d'error a l'usuari. Si el camp és vàlid, s'eliminarà qualsevol missatge d'error anterior relacionat amb el camp d'usuari.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtUser_Leave(object sender, EventArgs e)
        {
            if (txtUser.TextLength < 4)
            {
                if (txtUser.Text == "")
                {
                    lblErrorUser.Text = "El camp usuari és obligatori";
                }
                else
                {
                    lblErrorUser.Text = "El camp usuari ha de tenir almenys 4 caràcters";
                }
            }
            else
            {
                lblErrorUser.Text = "";
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que esborra qualsevol missatge d'error relacionat amb el camp d'usuari quan l'usuari comença a escriure. 
        /// Això permet que els missatges d'error siguin més dinàmics i no molestin a l'usuari mentras corregeix les seves dades.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtUser_TextChanged(object sender, EventArgs e)
        {
            lblErrorUser.Text = "";
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per validar el camp de contrasenya quan es deixa de fer focus. Si el camp està buit o té menys de 4 
        /// caràcters, es mostrarà un missatge d'error a l'usuari. Si el camp és vàlid, s'eliminarà qualsevol missatge 
        /// d'error anterior relacionat amb el camp de contrasenya.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtPassword_Leave(object sender, EventArgs e)
        {
            if (txtPassword.TextLength < 4)
            {
                if (txtPassword.Text == "")
                {
                    lblErrorPassword.Text = "El camp contrasenya és obligatori";
                }
                else
                {
                    lblErrorPassword.Text = "El camp contrasenya ha de tenir almenys 4 caràcters";
                }
            }
            else
            {
                lblErrorPassword.Text = "";
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que esborra qualsevol missatge d'error relacionat amb el camp de contrasenya quan l'usuari comença a escriure.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtPassword_TextChanged(object sender, EventArgs e)
        {
            lblErrorPassword.Text = "";
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per validar que els camps de contrasenya i confirmació de contrasenya coincideixin quan es deixa de fer focus al camp de confirmació.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void textBox2_Leave(object sender, EventArgs e)
        {
            if (txtPassword.Text != txtConfirm.Text)
            {
                lblErrorConfirm.Text = "Les contrasenyes no coincideixen";

            }
            else
            {
                lblErrorConfirm.Text = "";
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per validar que els camps d'usuari, contrasenya i confirmació de contrasenya compleixin els requisits mínims abans de permetre el registre.
        /// Aquest mètode es crida al principi del mètode btnRegister_Click, i si retorna false, es mostrarà un missatge d'error general a l'usuari 
        /// indicant que hi ha algun error en els camps introduïts.
        /// </summary>
        /// <returns>True si tots els camps són vàlids, False en cas contrari.</returns>
        private bool ValidateForm()
        {
            bool userValid = txtUser.TextLength >= 4;
            bool passwordValid = txtPassword.TextLength >= 4 && txtPassword.Text == txtConfirm.Text;
            // confirmar també email

            return userValid && passwordValid;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan l'usuari fa clic al botó de registre. Aquest mètode primer valida els camps del formulari mitjançant el mètode 
        /// ValidateForm. Si la validació falla, es mostrarà un missatge d'error general a l'usuari. Si la validació és correcta, es construirà 
        /// un objecte amb les dades de l'usuari, es serialitzarà a JSON i s'enviarà a l'API mitjançant una petició POST. La resposta de l'API es 
        /// gestiona per mostrar missatges d'èxit o error segons el cas. També es captura qualsevol excepció de connexió i es mostra un missatge 
        /// d'error adequat a l'usuari. Si el registre és exitós, es tanca el formulari per tornar al login.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Les dades de l'esdeveniment</param>
        private async void btnRegister_Click(object sender, EventArgs e)
        {
            if (!ValidateForm())
            {
                lblError.Text = "Algun camp introduït te un error";
                return;
            }

            // Construir l'objecte usuari
            var nouUsuari = new
            {
                name = txtUser.Text,
                password = txtPassword.Text,
                email = txtMail.Text,
            };

            var json = JsonSerializer.Serialize(nouUsuari);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            try
            {
                using (var client = new HttpClient())
                {
                    string url = "http://10.2.3.145:8080/api/users/newuser";

                    var resposta = await client.PostAsync(url, content);

                    if (resposta.StatusCode == System.Net.HttpStatusCode.Created)
                    {
                        MessageBox.Show("Usuari creat correctament!");

                        this.Close(); // Tornar al login
                    }
                    else if (resposta.StatusCode == System.Net.HttpStatusCode.BadRequest)
                    {
                        lblError.Text = "Format incorrecte. Revisa les dades.";
                        lblError.Visible = true;
                    }
                    else if (resposta.StatusCode == System.Net.HttpStatusCode.Conflict)
                    {
                        lblError.Text = "Aquest nom d'usuari ja existeix.";
                        lblError.Visible = true;
                    }
                    else
                    {
                        lblError.Text = "Error inesperat: " + resposta.StatusCode;
                        lblError.Visible = true;
                    }
                }
            }
            catch (Exception ex)
            {
                lblError.Text = "Error de connexió: " + ex.Message;
                lblError.Visible = true;
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per validar que el format del correu electrònic introduït sigui vàlid. 
        /// Aquest mètode utilitza una expressió regular per comprovar que el correu electrònic segueix un format bàsic,
        /// amb un nom d'usuari, un símbol '@' i un domini. Si el format és vàlid, retorna true; en cas contrari, retorna false.
        /// </summary>
        /// <param name="email">El correu electrònic a validar</param>
        /// <returns>True si el correu electrònic és vàlid, en cas contrari false</returns>
        private bool EsEmailValid(string email)
        {
            return Regex.IsMatch(email,
                @"^[^@\s]+@[^@\s]+\.[^@\s]+$",
                RegexOptions.IgnoreCase);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan es deixa de fer focus al camp de correu electrònic. Aquest mètode valida el format del correu
        /// electrònic introduït mitjançant el mètode EsEmailValid. Si el format no és vàlid, es mostrarà un missatge 
        /// d'error a l'usuari indicant que el correu electrònic no és vàlid. Si el format és correcte, s'eliminarà qualsevol 
        /// missatge d'error anterior relacionat amb el camp de correu electrònic. Aquesta validació ajuda a assegurar que els usuaris 
        /// introdueixin un correu electrònic amb un format correcte abans de permetre el registre. 
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void textBox1_Leave(object sender, EventArgs e)
        {
            if (!EsEmailValid(txtMail.Text))
            {
                lblErrorMail.Text = "Correu electrònic no vàlid";
                lblErrorMail.Visible = true;               
            }
            else
            {
                lblErrorMail.Visible = false;                
            }
        }
    }
}
