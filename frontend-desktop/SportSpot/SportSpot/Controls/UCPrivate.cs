using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Text;
using System.Text.Json;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SportSpot.Controls
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe per mostra les dades de l'usuari actual i permet modificar-les. També permet canviar la contrasenya.
    /// </summary>
    public partial class UCPrivate : UserControl
    {
        public UCPrivate()
        {
            InitializeComponent();
            _ = CarregarPerfilAsync();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Carrega les dades del perfil de l'usuari actual.
        /// </summary>
        /// <returns>Tasca que representa l'operació asincrònica.</returns>
        private async Task CarregarPerfilAsync()
        {
            try
            {
                using (var client = new HttpClient())
                {
                    client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

                    var resposta = await client.GetAsync("http://10.2.3.145:8080/api/users/me");

                    if (resposta.IsSuccessStatusCode)
                    {
                        var json = await resposta.Content.ReadAsStringAsync();

                        var usuari = JsonSerializer.Deserialize<Usuari>(json);

                        txtNom.Text = usuari.name;
                        txtMail.Text = usuari.email;
                        txtRol.Text = usuari.role;
                        chkActiu.Checked = usuari.active;
                    }
                    else
                    {
                        MessageBox.Show("Error carregant perfil: " + resposta.StatusCode);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de connexió: " + ex.Message);
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Configura el mode edició dels controls de formulari, habiliant o deshabilitant l'edició dels camps i ajustant 
        /// l'aparença dels botons segons correspongui.        
        /// </summary>
        /// <param name="edicio">Indica si s'ha d'activar el mode edició. Si és <see langword="true"/>, els controls
        /// s'habiliten per l'edició; si es <see langword="false"/>, es deshabilitan.</param>
        private void ModeEdicio(bool edicio)
        {
            txtNom.ReadOnly = !edicio;
            txtNom.Enabled = edicio;
            txtMail.ReadOnly = !edicio;
            txtMail.Enabled = edicio;

            chkActiu.AutoCheck = edicio;

            btnEditar.Text = edicio ? "Desar dades" : "Editar Dades";
            btnEditar.BackColor = edicio ? Color.FromArgb(255, 106, 0) : Color.FromArgb(51, 102, 204);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Desa de forma asincrònica les modificacions realitzades al perfil de l'usuari actual,
        /// enviant una petició PUT a l'API REST amb les dades actualitzades.
        /// </summary>
        /// <returns>Una tasca que representa l'operació asincrònica.</returns>
        private async Task DesarCanvisAsync()
        {
            try
            {
                var usuariModificat = new
                {
                    name = txtNom.Text,
                    email = txtMail.Text,
                };

                var json = JsonSerializer.Serialize(usuariModificat);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                using (var client = new HttpClient())
                {
                    client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

                    string url = $"http://10.2.3.145:8080/api/users/{Session.user}";

                    var resposta = await client.PutAsync(url, content);

                    if (resposta.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Dades actualitzades correctament");

                        // Actualitzar dades de sessió
                        Session.user = txtNom.Text;
                        Session.email = txtMail.Text;
                    }
                    else if (resposta.StatusCode == System.Net.HttpStatusCode.Forbidden)
                    {
                        MessageBox.Show("No tens permisos per modificar aquest usuari");
                    }
                    else if (resposta.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        MessageBox.Show("Usuari no trobat");
                    }
                    else
                    {
                        MessageBox.Show("Error actualitzant dades: " + resposta.StatusCode);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de connexió: " + ex.Message);
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Valida si els canvis realitzats al formulari són vàlids abans de desar-los, 
        /// comprovant que el nom d'usuari té almenys 4 caràcters
        /// </summary>
        /// <returns>Retorna si els canvis són vàlids</returns>
        private bool ValidateChanges()
        {
            bool userValid = txtNom.TextLength >= 4;
            bool mailValid = EsEmailValid(txtMail.Text);

            return userValid && mailValid;
        }


        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// El botó "Editar Dades" canvia al mode edició, habilitant els camps per modificar les dades de l'usuari.
        /// Canvia la seva aprença per indicar que ara és un botó de "Desar dades". Quan es torna a clicar,
        /// valida les dades i, si són vàlides,
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private async void btnEditar_Click(object sender, EventArgs e)
        {
            if (btnEditar.Text == "Editar Dades")
            {
                // Activem mode edició
                ModeEdicio(true);
            }
            else if (!ValidateChanges())
            {
                return;
            }
            else
            {
                // Mode desar dades
                await DesarCanvisAsync();

                // Tornem a mode lectura
                ModeEdicio(false);
            }
        }

        private void UCPrivate_Load(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Determina si la nova contrasenya introduïda és vàlida, comprovant que té almenys 4 caràcters i
        /// que coincideix amb el camp de confirmació.
        /// </summary>
        /// <returns>Retorna si la nova contrasenya és vàlida</returns>
        private bool ValidateNewPassword()
        {
            bool passwordValid = txtPassword.TextLength >= 4 && txtPassword.Text == txtConfirm.Text;

            return passwordValid;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Modifica la seva aparença i funcionalitat per passar a mode edició, habilitant els camps de contrasenya i
        /// confirmació, i canviant el text del botó a "Desar Contrasenya".
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private async void btnNewPwd_Click(object sender, EventArgs e)
        {
            if (btnNewPwd.Text == "Canviar Contrasenya")
            {
                // Activem mode edició
                ModeEdicioPwd(true);
            }
            else if (!ValidateNewPassword())
            {
                return;
            }
            else
            {
                // Cridem funció per desar nova contrasenya
                await DesarPwdAsync();

                ModeEdicioPwd(false);
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Actualitza de manera asíncrona la contrasenya de l'usuari actual, enviant una petició PUT 
        /// a l'API REST amb la nova contrasenya.
        /// </summary>
        /// <returns>Una tasca que representa l'operació asíncrona</returns>
        private async Task DesarPwdAsync()
        {
            try
            {
                var usuariModificat = new
                {
                    password = txtPassword.Text,
                };

                var json = JsonSerializer.Serialize(usuariModificat);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                using (var client = new HttpClient())
                {
                    client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken);

                    string url = $"http://10.2.3.145:8080/api/users/{Session.user}";

                    var resposta = await client.PutAsync(url, content);

                    if (resposta.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Dades actualitzades correctament");

                        // Actualitzar dades de sessió
                        Session.user = txtNom.Text;
                        Session.email = txtMail.Text;
                    }
                    else if (resposta.StatusCode == System.Net.HttpStatusCode.Forbidden)
                    {
                        MessageBox.Show("No tens permisos per modificar aquest usuari");
                    }
                    else if (resposta.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        MessageBox.Show("Usuari no trobat");
                    }
                    else
                    {
                        MessageBox.Show("Error actualitzant dades: " + resposta.StatusCode);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de connexió: " + ex.Message);
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Configura la UI en mode edició per al canvi de contrasenya, habilitant els camps de contrasenya i confirmació,
        /// i ajustant l'aparença del botó per indicar que ara és un botó de "Desar Contrasenya". Quan es desactiva el mode edició,
        /// els camps de contrasenya i confirmació es deshabiliten i el botó torna a ser un botó de "Canviar Contrasenya".
        /// </summary>
        /// <param name="edicio">Indica si s'ha d'activar o desactivar el mode edició</param>
        private void ModeEdicioPwd(bool edicio)
        {
            lblPassword.Visible = edicio;
            txtPassword.Visible = edicio;
            lblErrorPassword.Visible = edicio;
            lblConfirm.Visible = edicio;
            txtConfirm.Visible = edicio;
            lblErrorPassword.Visible = edicio;

            btnNewPwd.Text = edicio ? "Desar Contrasenya" : "Canviar Contrasenya";
            btnNewPwd.BackColor = edicio ? Color.FromArgb(255, 106, 0) : Color.FromArgb(51, 102, 204);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Esborra el missatge d'error del camp de nom d'usuari quan es comença a escriure, 
        /// permetent que l'usuari sàpiga que està corregint l'error i que el missatge d'error ja no és vàlid.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtNom_TextChanged(object sender, EventArgs e)
        {
            lblErrorUser.Text = "";
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Determina si la cadena (mail) té un format vàlid d'adreça de correu electrònic, comprovant 
        /// que conté un símbol "@" i un domini després d'aquest.
        /// </summary>
        /// <param name="email">La cadena que representa l'adreça de correu electrònic a validar</param>
        /// <returns>Un boleà que indica la correcció del format de l'adreça de correu electrònic</returns>
        private bool EsEmailValid(string email)
        {
            return Regex.IsMatch(email,
                @"^[^@\s]+@[^@\s]+\.[^@\s]+$",
                RegexOptions.IgnoreCase);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Es valida si el correu electrònic introduït té un format vàlid quan es perd el focus del 
        /// camp de correu electrònic, mostrant un missatge d'error si el format no és correcte o amagant
        /// el missatge d'error si el format és vàlid.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtMail_Leave(object sender, EventArgs e)
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

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Elimina el missatge d'error mentre s'edita el camp de contrasenya, indicant que l'usuari 
        /// està corregint l'error i que el missatge d'error ja no és vàlid.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtPassword_TextChanged(object sender, EventArgs e)
        {
            lblErrorPassword.Text = "";
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Validació de la nova contrasenya introduïda quan es perd el focus del camp de contrasenya, comprovant 
        /// que té almenys 4 caràcters i mostrant un missatge d'error si no compleix aquest requisit o amagant 
        /// el missatge d'error si és vàlida.
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
        /// Validació de la confirmació de la nova contrasenya introduïda quan es perd el focus del camp de confirmació,
        /// comprovant que coincideix amb el camp de contrasenya i mostrant un missatge d'error si no coincideixen 
        /// o amagant el missatge
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtConfirm_Leave(object sender, EventArgs e)
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
        /// Validació del nom d'usuari introduït quan es perd el focus del camp de nom d'usuari, comprovant
        /// que té almenys 4 caràcters i mostrant un missatge d'error si no compleix aquest requisit o amagant 
        /// el missatge d'error si és vàlid.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtNom_Leave(object sender, EventArgs e)
        {
            if (txtNom.TextLength < 4)
            {
                if (txtNom.Text == "")
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
        /// Mètode que tanca la sessió de l'usuari actual, netejant les dades de sessió i obrint el 
        /// formulari de login, permetent que l'usuari torni a iniciar sessió o iniciï sessió amb un altre 
        /// compte. També tanca el formulari actual per assegurar-se que l'usuari no pugui accedir a les 
        /// dades del perfil després de tancar sessió.
        /// </summary>
        private void TancarSessioIRetornarAlLogin()
        {
            // Netejem dades de sessió
            Session.sessionToken = null;
            Session.user = null;
            Session.role = null;

            // obrim el formulari de login
            var loginForm = new LoginForm();
            loginForm.Show();

            // Tanquem el formulari actual
            Form parentForm = this.FindForm();
            parentForm.Close();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que s'executa quan es clica el botó "Eliminar Compte". Mostra un missatge de confirmació 
        /// per assegurar-se que l'usuari vol eliminar el seu compte, ja que aquesta acció no es pot desfer. 
        /// Si l'usuari confirma, es fa una petició DELETE a l'API REST per eliminar el compte de l'usuari actual.
        /// Si la eliminació és exitosa, es mostra un missatge de confirmació i es tanca la sessió retornant 
        /// al formulari de login. Si hi ha errors, es mostren missatges d'error adequats segons el codi de 
        /// resposta de l'API.
        /// </summary>
        /// <param name="sender">L'objecte que va generar l'esdeveniment</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private async void btnEliminarCompte_Click(object sender, EventArgs e)
        {
            var result = MessageBox.Show(
                "Segur que vols eliminar el teu compte? Aquesta acció no es pot desfer.",
                "Confirmar eliminació",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning
            );

            if (result != DialogResult.Yes)
            {
                return; // Si diu No, sortim i no fem res més
            }

            // Aquí ja fem la crida a l'API per eliminar el compte
            using (var client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://10.2.3.145:8080/");                
                client.DefaultRequestHeaders.Add("Session-Token", Session.sessionToken); 

                var userName = Session.user;                              
                
                var response = await client.DeleteAsync($"api/users/{userName}");
                
                if (response.StatusCode == HttpStatusCode.NoContent)
                {
                    MessageBox.Show("El teu compte s'ha eliminat correctament.", "Compte eliminat",
                        MessageBoxButtons.OK, MessageBoxIcon.Information);

                    // Aquí tanquem sessió i tornem al login
                    TancarSessioIRetornarAlLogin();
                }
                else if (response.StatusCode == HttpStatusCode.Forbidden)
                {
                    MessageBox.Show("No tens permisos per eliminar aquest compte.", "Operació no permesa",
                        MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
                else if (response.StatusCode == HttpStatusCode.NotFound)
                {
                    MessageBox.Show("No s'ha trobat l'usuari.", "No trobat",
                        MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
                else
                {
                    MessageBox.Show($"Error en eliminar el compte. Codi: {(int)response.StatusCode}",
                        "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
        }
    }
}
