const { createApp } = Vue;

createApp({
    data() {
        return {
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            confirmPassword: "",
            mostrarRegistro: true,
        }
    },
    created() {

    },
    methods: {
        login() {
            axios.post('http://localhost:8080/api/login', "email=" + this.email + "&password=" + this.password)
                .then(response => {
                    location.href = "http://localhost:8080/web/accounts.html";
                })
                .catch(e => {
                    console.log(e);
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Bad Credentials!'
                    })
                    this.email = ""
                    this.password = ""
                });
        },

        registrar() {

            if (this.firstName.length == 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Invalid First Name!'
                })
                this.firstName = "";
            }else if(this.lastName.length == 0){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Invalid Last Name!'
                })
                this.lastName = "";
            }else if(this.email.length == 0 || !this.email.includes("@")){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Invalid Email!'
                })
                this.email = "";
            }else if(this.password.length < 8){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Invalid Password!'
                })
            }else if(this.password != this.confirmPassword){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Passwords do not match!'
                })
                this.password = "";
                this.confirmPassword = "";
            }else{
                this.confirmarRegistro();
            }

        },

        confirmarRegistro(){
            Swal.fire({
                title: 'Are you sure?',
                text: "An account will also be created in our bank so that you can make purchases and movements without charge.",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes!'
            }).then(r => {
                if (r.isConfirmed){
                    axios.post('http://localhost:8080/api/clients', 
                        "firstname=" + this.firstName +
                        "&lastname=" + this.lastName +
                        "&email=" + this.email +
                        "&password=" + this.password)
                    .then(r =>{
                        Swal.fire({
                            icon: "success",
                            text: "Your account has been created.",
                        }).then(r => {
                            axios.post("http://localhost:8080/api/clients/current/accounts")
                                .then(r => {
                                    Swal.fire({
                                        icon: "success",
                                        text: "You will be redirected to another page.",
                                    }).then(r =>{
                                        location.href = "http://localhost:8080/web/accounts.html";
                                    })
                                })
                                .catch(e => console.log(e));
                        })
                    })
                    .catch(e => console.log(e));
                }else{
                    this.firstName = "";
                    this.lastName = "";
                    this.email = "";
                    this.password = "";
                }
            })
        }
    },
    computed: {

    }
}).mount("#app");