using Microsoft.VisualStudio.TestTools.UnitTesting;
using SportSpot.Models;
using SportSpot.Services;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;

namespace SportSpot.Tests
{
    [TestClass]
    public class UserServiceTests
    {
        // Handler simulat per interceptar peticions HTTP
        public class FakeHttpMessageHandler : HttpMessageHandler
        {
            private readonly HttpResponseMessage _fakeResponse;

            public FakeHttpMessageHandler(HttpResponseMessage response)
            {
                _fakeResponse = response;
            }

            protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
            {
                return Task.FromResult(_fakeResponse);
            }
        }

        [TestMethod]
        public async Task GetUsers_ReturnsListOfUsers()
        {
            var users = new List<Usuari>
    {
        new Usuari { name = "miki", email = "miki@test.com" },
        new Usuari { name = "anna", email = "anna@test.com" }
    };

            var fakeResponse = new HttpResponseMessage(HttpStatusCode.OK)
            {
                Content = new StringContent(JsonSerializer.Serialize(users))
            };

            var fakeClient = new HttpClient(new FakeHttpMessageHandler(fakeResponse))
            {
                BaseAddress = new Uri("https://fakeapi.test/")
            };

            var service = new UserService(fakeClient);

            var result = await service.GetUsers();

            Assert.AreEqual(2, result.Count);
            Assert.AreEqual("miki", result[0].name);
        }


        [TestMethod]
        public async Task GetMyProfile_ReturnsUser()
        {
            var user = new Usuari { name = "miki", email = "miki@test.com" };

            var fakeResponse = new HttpResponseMessage(HttpStatusCode.OK)
            {
                Content = new StringContent(JsonSerializer.Serialize(user))
            };

            var fakeClient = new HttpClient(new FakeHttpMessageHandler(fakeResponse))
            {
                BaseAddress = new Uri("https://fakeapi.test/")
            };

            var service = new UserService(fakeClient);

            var result = await service.GetMyProfile();

            Assert.AreEqual("miki", result.name);
            Assert.AreEqual("miki@test.com", result.email);
        }


        [TestMethod]
        public async Task DeleteUser_NoContent_ReturnsTrue()
        {
            var fakeResponse = new HttpResponseMessage(HttpStatusCode.NoContent);

            var fakeClient = new HttpClient(new FakeHttpMessageHandler(fakeResponse))
            {
                BaseAddress = new Uri("https://fakeapi.test/")
            };

            var service = new UserService(fakeClient);

            var result = await service.DeleteUser("miki");

            Assert.IsTrue(result);
        }
    }
}
