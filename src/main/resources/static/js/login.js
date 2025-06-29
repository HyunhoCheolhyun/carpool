// document.addEventListener("DOMContentLoaded", () => {
//     const form = document.getElementById("login-form");
//     const message = document.getElementById("message");
//
//     form.addEventListener("submit", async (e) => {
//         e.preventDefault();
//         message.textContent = "";
//
//         const username = document.getElementById("username").value.trim();
//         const password = document.getElementById("password").value.trim();
//
//         if (!username || !password) {
//             message.textContent = "아이디와 비밀번호를 입력해주세요.";
//             return;
//         }
//
//         try {
//             const response = await fetch("/api/login", {
//                 method: "POST",
//                 headers: {
//                     "Content-Type": "application/x-www-form-urlencoded"
//                 },
//                 body: new URLSearchParams({ username, password })
//             });
//
//             if (response.ok) {
//                 window.location.href = "/";
//             } else {
//                 message.textContent = "아이디 또는 비밀번호가 잘못되었습니다.";
//             }
//         } catch (err) {
//             console.error(err);
//             message.textContent = "서버 오류가 발생했습니다.";
//         }
//     });
// });
