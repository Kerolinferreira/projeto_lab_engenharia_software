const inpCep = document.getElementById('cep');
const inpLogradouro = document.getElementById('logradouro');
const inpNumero = document.getElementById('numero');
const inpBairro = document.getElementById('bairro');
const inpCidade = document.getElementById('cidade');
const inpUf = document.getElementById('uf');

const preencherCampos = (dadosEndereco) => {
    inpLogradouro.value = dadosEndereco.street || '';
    inpBairro.value = dadosEndereco.neighborhood || ''; 
    inpCidade.value = dadosEndereco.city || ''; 
    inpUf.value = dadosEndereco.state || ''; 
};

const buscarEnderecoPorCEP = async (cep) => {
    preencherCampos({}); 

    const cepLimpo = cep.replace(/\D/g, '');

    if (cepLimpo.length !== 8) {
        console.log("CEP inválido.");
        return;
    }

    try {
       
        inpLogradouro.value = "Buscando...";
        inpBairro.value = "...";
        inpCidade.value = "...";
        inpUf.value = "...";

       
        const apiResponse = await fetch(`https://brasilapi.com.br/api/cep/v1/${cepLimpo}`);

        
        if (!apiResponse.ok) {
           
            throw new Error('CEP não encontrado ou inválido.');
        }

        const dadosEndereco = await apiResponse.json();

       
        preencherCampos(dadosEndereco);
        
        
        inpNumero.focus();

    } catch (error) {
       
        console.error(error);
        alert(error.message);
        preencherCampos({});
    }
};


inpCep.addEventListener('blur', (event) => {
    const cepDigitado = event.target.value;
    if (cepDigitado) {
        buscarEnderecoPorCEP(cepDigitado);
    }
});


function salvarComoJson(dados, nomeArquivo = 'cadastro.json') {

    const jsonString = JSON.stringify(dados, null, 2);


    const blob = new Blob([jsonString], { type: 'application/json' });


    const url = URL.createObjectURL(blob);


    const link = document.createElement('a');
    link.href = url;
    link.download = nomeArquivo; 


    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);


    URL.revokeObjectURL(url);
}


document.addEventListener('DOMContentLoaded', () => {
    
    const form = document.querySelector('form');

    if (form) {
        form.addEventListener('submit', (event) => {
            
            event.preventDefault();

            const formData = new FormData(form);
            const dadosDoFormulario = Object.fromEntries(formData.entries());

            console.log('Dados capturados do formulário:');
            console.log(dadosDoFormulario);
            

            salvarComoJson(dadosDoFormulario, 'dados_cadastro.json');
        });
    } else {
        console.error('Nenhum formulário encontrado na página.');
    }
});

