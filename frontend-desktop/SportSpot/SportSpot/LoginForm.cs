using SportSpot.Models;
using SportSpot.Services;
using System.Linq.Expressions;

namespace SportSpot
{
    public partial class LoginForm : Form
    {
        private AuthService authService = new AuthService();
        public LoginForm()
        {
            InitializeComponent();

        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètodes per fer visible o no la constrasenya.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void chkShowPwd_CheckedChanged(object sender, EventArgs e)
        {
            if (chkShowPwd.Checked == true)
            {
                txtPassword.UseSystemPasswordChar = false;
            }
            else
            {
                txtPassword.UseSystemPasswordChar = true;
            }

        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per validar les dades d'entrada i cridar el servei d'autenticació. En funció del rol de l'usuari,
        /// s'obrirà un formulari diferent. Si hi ha algun error, es mostrarà un missatge a l'usuari.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private async void btnLogin_Click(object sender, EventArgs e)
        {
            if (!ValidateForm())
            {
                return;
            }

            // Agafar usuari i contrasenya del formulari
            string user = txtUser.Text;
            string password = txtPassword.Text;

            // Cridem el servei
            var result = await authService.LoginAsync(user, password);

            // Comprovem el resultat
            if (result.success)
            {
                Session.sessionToken = result.sessionToken;
                Session.role = result.role;
                Session.user = user;

                switch (result.role)
                {
                    case "ADMIN":
                        // Obrir el formulari d'administrador
                        var adminForm = new AdminForm();
                        adminForm.Show();
                        this.Hide();
                        break;

                    case "CLIENT":
                        // Obrir el formualri del client
                        var clientForm = new ClientForm();
                        clientForm.Show();
                        this.Hide();
                        break;

                    default:
                        lblError.Text = "Rol desconegut: " + result.role;
                        break;
                }
            }
            else
            {
                lblError.Text = result.message;
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per validar les dades del textbox d'usuari quan es perd el focus. Si el camp està buit, 
        /// es mostrarà un missatge d'error indicant que és obligatori. Si el camp té menys de 4 caràcters,
        /// es mostrarà un missatge d'error indicant que ha de tenir almenys 4 caràcters. 
        /// Si el camp és vàlid, s'esborrarà qualsevol missatge d'error anterior.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
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
        /// Mètode per validar les dades del textbox de contrasenya quan es perd el focus. Si el camp està buit, 
        /// es mostrarà un missatge d'error indicant que és obligatori. Si el camp té menys de 4 caràcters, 
        /// mostrarà un missatge d'error indicant que ha de tenir almenys 4 caràcters. Si el camp és vàlid, 
        /// s'esborrarà qualsevol missatge d'error anterior.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void txtPassword_Leave(object sender, EventArgs e)
        {
            if(txtPassword.TextLength < 4)
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
        /// Mètode que esborra qualsevol missatge d'error relacionat amb el camp d'usuari quan l'usuari comença
        /// a escriure. Això permet que els missatges d'error siguin més dinàmics i no molestin a l'usuari 
        /// mentre corregeix les seves dades.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void txtUser_TextChanged(object sender, EventArgs e)
        {
            lblErrorUser.Text = "";            
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que esborra qualsevol missatge d'error relacionat amb el camp de contrasenya quan l'usuari 
        /// comença a escriure. Això permet que els missatges d'error siguin més dinàmics i no molestin a l'usuari 
        /// mentras corregeix les seves dades.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void txtPassword_TextChanged(object sender, EventArgs e)
        {
            lblErrorPassword.Text = "";            
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per validar que els camps user i password compleixin els requisits mínims abans de permetre 
        /// l'intent de login. Aquest mètode es crida al principi del mètode btnLogin_Click, i si retorna false,
        /// no es procedirà amb la crida al servei d'autenticació. Això ajuda a evitar crides innecessàries 
        /// al servei quan les dades d'entrada no són vàlides.
        /// </summary>
        /// <returns></returns>
        private bool ValidateForm()
        {
            bool userValid = txtUser.Text.Length >= 4;
            bool passwordValid = txtPassword.Text.Length >= 4;

            return userValid && passwordValid;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per tancar l'aplicació
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void LoginForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            Application.Exit();
        }
    }
}
