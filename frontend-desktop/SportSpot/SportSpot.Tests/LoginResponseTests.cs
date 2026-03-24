using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Tests
{
    /// <summary>
    /// Proves unitàries del model LoginResponse.
    /// Autor: Miquel Uribe Faixedas
    /// </summary>
    [TestClass]
    public class LoginResponseTests
    {
        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que les propietats s'assignen correctament.
        /// </summary>
        [TestMethod]
        public void LoginResponse_AssignsPropertiesCorrectly()
        {
            var response = new LoginResponse
            {
                success = true,
                message = "Login test correcte",
                resultCode = 200,
                sessionToken = "abc123",
                role = "admin"
            };

            Assert.IsTrue(response.success);
            Assert.AreEqual(200, response.resultCode);
            Assert.AreEqual("abc123", response.sessionToken);
            Assert.AreEqual("admin", response.role);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que les propietats tenen els valors per defecte correctes quan
        /// s'instancia l'objecte.
        /// </summary>
        [TestMethod]
        public void LoginResponse_DefaultValuesAreCorrect()
        {
            var response = new LoginResponse();

            Assert.IsFalse(response.success);
            Assert.IsNull(response.message);
            Assert.AreEqual(0, response.resultCode);
            Assert.IsNull(response.sessionToken);
            Assert.IsNull(response.role);
        }
    }
}