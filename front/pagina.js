const inpCep = document.getElementById('cep');
const inpLogradouro = document.getElementById('logradouro');
const inpNumero = document.getElementById('numero');
const inpBairro = document.getElementById('bairro');
const inpCidade = document.getElementById('cidade');
const inpUf = document.getElementById('uf');
const slcTipo = document.getElementById('tipo');
const inpCpfCnpj = document.getElementById('cpf_cnpj');
const inpNome = document.getElementById('nome');
const inpDataNasc = document.getElementById('data_nasc');


const preencherCamposComDadosCnpj = (dadosCnpj) => {
    inpNome.value = dadosCnpj.razao_social || '';
    inpDataNasc.value = dadosCnpj.estabelecimento.data_inicio_atividade || '';
};

const buscarDadosPorCNPJ = async (cnpj) => {
    if (slcTipo.value !== 'pj') {
        return;
    }

    const cnpjLimpo = cnpj.replace(/\D/g, '');


    try {
        inpNome.value = "Buscando dados do CNPJ...";
        inpDataNasc.value = "";
        const apiResponse = await fetch(`https://publica.cnpj.ws/cnpj/${cnpjLimpo}`);

        if (!apiResponse.ok) {
            if (apiResponse.status === 404) {
                 throw new Error('CNPJ não encontrado');
            }
             if (apiResponse.status === 429) {
                 throw new Error('Tente novamente em breve');
            }
            throw new Error('Não foi possível consultar o CNPJ');
        }

        const dadosCnpj = await apiResponse.json();
        
        preencherCamposComDadosCnpj(dadosCnpj);

    } catch (error) {
        console.error(error);
        alert(error.message);
        inpNome.value = "";
    }
};

inpCpfCnpj.addEventListener('blur', (event) => {
    const cnpjinp = event.target.value;
    if (cnpjinp) {
        buscarDadosPorCNPJ(cnpjinp);
    }
});



const preencherCamposEndereco = (dadosEndereco) => {
    inpLogradouro.value = dadosEndereco.street || '';
    inpBairro.value = dadosEndereco.neighborhood || ''; 
    inpCidade.value = dadosEndereco.city || ''; 
    inpUf.value = dadosEndereco.state || ''; 
};

const buscarEnderecoPorCEP = async (cep) => {
    preencherCamposEndereco({}); 

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
        
        preencherCamposEndereco(dadosEndereco);
        inpNumero.focus();

    } catch (error) {
        console.error(error);
        alert(error.message);
        preencherCamposEndereco({});
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
    const submitButton = document.getElementById('submit');
    
    if (form && submitButton) {
        form.addEventListener('submit', async (event) => {
            event.preventDefault();

            if (!form.checkValidity()) {
                form.reportValidity(); 
                return; 
            }

            const formData = new FormData(form);
            const dadosPlanos = Object.fromEntries(formData.entries());
            

            const dadosfim = {
    cpf_cnpj: dadosPlanos.cpf_cnpj,
    tipo: dadosPlanos.tipo,
    nome: dadosPlanos.nome,
    nome_fantasia: dadosPlanos.nome_fantasia || null,
    data_abertura_nascimento: dadosPlanos.data_abertura_nascimento || null,
    homepage: dadosPlanos.homepage || null,
    email: dadosPlanos.email,
    nome_contato: dadosPlanos.nome_contato || null,
    contato: dadosPlanos.contato,
    endereco: {
        cep: dadosPlanos.cep.replace(/\D/g, ''),
        logradouro: dadosPlanos.logradouro,
        numero: dadosPlanos.numero,
        complemento: dadosPlanos.complemento || null,
        bairro: dadosPlanos.bairro,
        cidade: dadosPlanos.cidade,
        uf: dadosPlanos.uf,
        pais: "Brasil"
    }
};

;
                        
            submitButton.disabled = true;
            submitButton.textContent = 'Finalizando cadastro...';

            try {
                const response = await fetch('http://localhost:8081/api/clientes', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(dadosfim),
                    
                });
                console.log("dados enviados");

                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || `Erro ${response.status}`);
                }
                
                const resultado = await response.json();
                // Atualiza variável global
                alert('Cadastro realizado com sucesso!');
                form.reset();


            } catch (error) {
                console.error('Falha ao enviar o formulário:', error);
                alert(`Erro no cadastro: ${error.message}`);
            } finally {
                submitButton.disabled = false;
                submitButton.textContent = 'ENVIAR';
            }
        });
    } else {
        console.error('Formulário ou botão de submit não encontrado na página.');
    }
});