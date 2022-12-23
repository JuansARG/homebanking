const { createApp } = Vue;

createApp({
    data(){
        return {
            firstName : "",
            lastName : "",
            email: "",
            password: "",
            mostrarRegistro: true,
        }
    },
    created(){
        
    },
    methods: {
        login(){
            axios.post('http://localhost:8080/api/login', "email=" + this.email + "&password=" + this.password)
            .then(response => {
                location.href = "http://localhost:8080/web/accounts.html";
            })
            .catch(e => {
                alert("Bad Credentials");
                this.email = ""
                this.password = ""
            });
        },

        registrar(){
            axios.post('http://localhost:8080/api/clients', "firstname=" + this.firstName +
                                                            "&lastname=" + this.lastName +
                                                            "&email=" + this.email + 
                                                            "&password=" + this.password)
            .then(response => {
                location.href = "http://localhost:8080/web/accounts.html";
            })
            .catch(e => console.log(e));
        }
    },
    computed: {
        
    }
}).mount("#app");