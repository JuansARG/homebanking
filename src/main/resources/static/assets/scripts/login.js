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
            if(this.email == "" || !this.email.includes("@")){
                this.alertaError("Invalid Email!")
            }else if(this.password == ""){
                this.alertaError("Invalid Password!")
            }else{
                axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                .then(() => location.href = "/web/accounts.html")
                .catch((e) => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Incorrect data.'
                    })
                    this.email = ""
                    this.password = ""
                    console.log(e);
                });
            }
            
        },

        registrar() {
            if (this.firstName.length == 0) {
                this.alertaError("Invalid First Name!")
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
                    text: 'Invalid password, must be at least 8 characters!'
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
                    axios.post('/api/clients', 
                        `firstname=${this.firstName}&lastname=${this.lastName}&email=${this.email}&password=${this.password}`)
                    .then(() =>{
                        Swal.fire({
                            icon: "success",
                            text: "Your account has been created.",
                        }).then(() => {
                            axios.post('/api/login', `email=${this.email}&password=${this.password}`)
                                .then(() => {
                                    axios.post("/api/clients/current/accounts", `type=CURRENT`)
                                        .then(() => {
                                            Swal.fire({
                                                icon: "success",
                                                text: "You will be redirected to another page.",
                                            }).then(() => location.href = "/web/accounts.html")
                                        })
                                        .catch(e => console.log(e))
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
                    this.confirmPassword = "";
                }
            })
        },
        alertaError(mensaje){
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: mensaje
            })
        }
    },
    computed: {
    }
}).mount("#app");