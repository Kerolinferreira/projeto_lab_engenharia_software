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

// Adiciona o listener de CNPJ apenas quando o campo existir (página de cadastro).
if (inpCpfCnpj) {
    inpCpfCnpj.addEventListener('blur', (event) => {
        const cnpjinp = event.target.value;
        if (cnpjinp) {
            buscarDadosPorCNPJ(cnpjinp);
        }
    });
}



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

// Adiciona o listener de CEP apenas quando o campo existir (página de cadastro).
if (inpCep) {
    inpCep.addEventListener('blur', (event) => {
        const cepDigitado = event.target.value;
        if (cepDigitado) {
            buscarEnderecoPorCEP(cepDigitado);
        }
    });
}





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

/*POPUP*/

const popupEmail = document.getElementById("popup-email");
const popupEmailClose = document.getElementById("popup-email-close");
const popupEmailOk = document.getElementById("popup-email-ok");
const popupEmailInput = document.getElementById("popup-email-input");

popupEmailClose.addEventListener("click", () => {
    popupEmail.style.display = "none";
});
                        
            submitButton.disabled = true;
            submitButton.textContent = 'Finalizando cadastro...';

            try {
                const response = await fetch('/api/clientes', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    // Desabilita cache para garantir que o backend receba e retorne dados atualizados
                    cache: 'no-store',
                    body: JSON.stringify(dadosfim)
                });
                console.log("dados enviados");

                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || `Erro ${response.status}`);
                }

               
                setTimeout(() => {
    popupEmailInput.value = '';
    popupEmail.style.display = "flex";
}, 0);


popupEmailOk.addEventListener("click", async () => {
    const emailDestino = popupEmailInput.value.trim();
    if (!emailDestino || !emailDestino.includes("@")) {
        alert("Digite um e-mail válido!");
        return;
    }

    try {
        await fetch('http://localhost:8081/api/clientes/email', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ emailDestino }),
  });

        popupEmail.style.display = "none";
        alert("E-mail enviado com sucesso!");
    } catch (err) {
        alert("Falha ao enviar e-mail: " + err.message);
    }
});

                
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


// ===================================================================
// === ADIÇÃO: Lista de Clientes (render / editar / excluir)        ===
// ===================================================================
(() => {
  // DOM refs da lista/modal
  const TBODY      = document.getElementById('tbody');
  const EMPTY_EL   = document.getElementById('empty');
  const SEARCH_INP = document.getElementById('search');
  const CLEAR_BTN  = document.getElementById('clearBtn');

  const DIALOG   = document.getElementById('editDialog');
  const FORM_MOD = document.getElementById('editForm');

  // Campos do modal
  const FM = {
    idHidden:      document.getElementById('f_id'),
    idDisplay:     document.getElementById('f_id_display'),
    cpf_cnpj:      document.getElementById('f_cpf_cnpj'),
    tipo:          document.getElementById('f_tipo'),
    nome:          document.getElementById('f_nome'),
    nome_fantasia: document.getElementById('f_nome_fantasia'),
    data_nasc:     document.getElementById('f_data_nasc'),
    homepage:      document.getElementById('f_homepage'),
    email:         document.getElementById('f_email'),
    nome_contato:  document.getElementById('f_nome_contato'),
    contato:       document.getElementById('f_contato'),
    cep:           document.getElementById('f_cep'),
    logradouro:    document.getElementById('f_logradouro'),
    numero:        document.getElementById('f_numero'),
    complemento:   document.getElementById('f_complemento'),
    bairro:        document.getElementById('f_bairro'),
    cidade:        document.getElementById('f_cidade'),
    pais:          document.getElementById('f_pais'),
    uf:            document.getElementById('f_uf'),
  };

  // Lista de clientes carregada do backend.
  // Em vez de utilizar um conjunto de dados de exemplo como anteriormente,
  // inicializamos a lista vazia. Os dados serão obtidos através das
  // chamadas à API Spring Boot expostas em `/api/clientes`.
  let clients = [];

  /**
   * Busca todos os clientes cadastrados no backend. Esta função é
   * chamada inicialmente e também utilizada ao limpar a pesquisa. Ela
   * atualiza a variável global `clients` com o retorno da API e,
   * em seguida, invoca o renderizador da tabela.
   */
  async function fetchAllClients() {
    if (!TBODY) return;
    try {
      // Use caminho absoluto e desabilite cache para garantir dados atualizados
      const resp = await fetch('/api/clientes/listarClientes', {
        method: 'GET',
        cache: 'no-store'
      });
      if (!resp.ok) throw new Error('Falha ao carregar clientes');
      clients = await resp.json();
      render(SEARCH_INP?.value || '');
    } catch (err) {
      console.error(err);
      clients = [];
      render('');
    }
  }

  /**
   * Realiza uma consulta na API para buscar clientes pelo texto fornecido.
   * A pesquisa considera id, nome ou cidade, conforme implementado
   * no serviço do backend. Se a pesquisa falhar, mantém a lista
   * atual e apenas registra o erro no console.
   * @param {string} texto termo de busca
   */
  async function fetchSearch(texto) {
    if (!TBODY) return;
    const q = (texto || '').trim();
    // Se não houver texto de busca, re-carrega todos os clientes
    if (!q) {
      await fetchAllClients();
      return;
    }
    try {
      const resp = await fetch('/api/clientes/buscarPorTexto?texto=' + encodeURIComponent(q), {
        method: 'GET',
        cache: 'no-store'
      });
      if (!resp.ok) throw new Error('Falha na busca');
      clients = await resp.json();
      render(q);
    } catch (err) {
      console.error(err);
    }
  }

  // Render da tabela
  function render(filter = '') {
    if (!TBODY) return;
    TBODY.innerHTML = '';
    const q = (filter || '').trim().toLowerCase();

    const filtered = clients.filter(c => {
      if (!q) return true;
      // Gera um array com os campos pesquisáveis: id, nome, nome fantasia,
      // CPF/CNPJ e cidade do endereço. Qualquer correspondência parcial
      // (case-insensitive) manterá o registro na lista filtrada.
      const cidade = (c.endereco && c.endereco.cidade) || '';
      return [
        c.id,
        c.nome,
        c.nome_fantasia,
        c.cpf_cnpj,
        cidade
      ].some(v => v && String(v).toLowerCase().includes(q));
    });

    if (EMPTY_EL) EMPTY_EL.hidden = filtered.length !== 0;

    for (const c of filtered) {
      const tr = document.createElement('tr');
      // Para exibir corretamente os dados vindos do backend, mapeamos os campos
      // da API para as colunas esperadas. O código passa a exibir o ID gerado,
      // o nome como razão social, o nome fantasia (quando presente), o CPF/CNPJ
      // e a cidade do endereço.
      tr.innerHTML = `
        <td><span class="mono">${escapeHtml(c.id ?? '')}</span></td>
        <td>${escapeHtml(c.nome || '')}</td>
        <td>${escapeHtml(c.nome_fantasia || '')}</td>
        <td><span class="mono">${escapeHtml(c.cpf_cnpj || '')}</span></td>
        <td>${escapeHtml((c.endereco && c.endereco.cidade) || '')}</td>
        <td>
          <div class="actions">
            <button type="button" class="table-btn edit">Editar</button>
            <button type="button" class="table-btn del">Excluir</button>
          </div>
        </td>
      `;

      tr.querySelector('.edit')?.addEventListener('click', () => openEdit(c.id));
      tr.querySelector('.del')?.addEventListener('click', () => delClient(c.id));

      TBODY.appendChild(tr);
    }
  }

  function escapeHtml(str) {
    if (!str) return '';
    return String(str).replaceAll('&', '&amp;')
                      .replaceAll('<', '&lt;')
                      .replaceAll('>', '&gt;')
                      .replaceAll('"', '&quot;')
                      .replaceAll("'", '&#39;');
  }

  // Busca / limpar
  // Ao limpar a busca, recarrega todos os clientes do backend.
  CLEAR_BTN?.addEventListener('click', async () => {
    if (SEARCH_INP) { SEARCH_INP.value = ''; }
    await fetchAllClients();
  });
  // A cada alteração no campo de pesquisa, envia a busca para a API.
  SEARCH_INP?.addEventListener('input', (e) => {
    const texto = e.target.value;
    fetchSearch(texto);
  });

  // Abrir modal e preencher
  function openEdit(id) {
    const c = clients.find(x => x.id === id);
    if (!c) { alert('Cliente não encontrado.'); return; }

    FM.idHidden.value       = c.id;
    FM.idDisplay.value      = c.id;
    // Preenche os campos com os dados retornados pela API. O DTO do backend
    // utiliza nomes de campo diferentes do mock antigo (e.g. endereco em um objeto).
    FM.cpf_cnpj.value       = c.cpf_cnpj || '';
    FM.tipo.value           = c.tipo || '';
    FM.nome.value           = c.nome || '';
    FM.nome_fantasia.value  = c.nome_fantasia || '';
    FM.data_nasc.value      = c.data_abertura_nascimento || '';
    FM.homepage.value       = c.homepage || '';
    FM.email.value          = c.email || '';
    FM.nome_contato.value   = c.nome_contato || '';
    FM.contato.value        = c.contato || '';
    const end = c.endereco || {};
    FM.cep.value            = end.cep || '';
    FM.logradouro.value     = end.logradouro || '';
    FM.numero.value         = end.numero || '';
    FM.complemento.value    = end.complemento || '';
    FM.bairro.value         = end.bairro || '';
    FM.cidade.value         = end.cidade || '';
    FM.pais.value           = end.pais || 'Brasil';
    FM.uf.value             = (end.uf || '').toString().toUpperCase();

    DIALOG?.showModal();
  }

  // Fechar modal
  document.getElementById('closeDialog')?.addEventListener('click', () => DIALOG?.close());
  document.getElementById('cancelBtn')?.addEventListener('click', (e) => { e.preventDefault(); DIALOG?.close(); });

  // Salvar edição
  FORM_MOD?.addEventListener('submit', (e) => {
    e.preventDefault();
    const id = Number(FM.idHidden.value || 0);
    if (!id) { alert('Edição sem ID não é permitida.'); return; }

    // Validação mínima (mantém seus required no HTML/CSS)
    const obrig = [
      [FM.cpf_cnpj, 'CPF/CNPJ'],
      [FM.tipo,     'Tipo'],
      [FM.nome,     'Nome'],
      [FM.email,    'Email'],
      [FM.contato,  'Contato'],
      [FM.logradouro, 'Logradouro'],
      [FM.numero,   'Número'],
      [FM.bairro,   'Bairro'],
      [FM.cidade,   'Cidade'],
      [FM.uf,       'UF'],
    ];
    for (const [el, label] of obrig) {
      if (!el || !el.value.trim()) { alert(`Preencha o campo obrigatório: ${label}.`); el?.focus(); return; }
    }

    // Monta o payload de atualização conforme o DTO de PUT do backend.
    const updatedPayload = {
      cpf_cnpj: FM.cpf_cnpj.value.trim(),
      tipo: FM.tipo.value,
      nome: FM.nome.value.trim(),
      nome_fantasia: FM.nome_fantasia.value.trim() || null,
      data_abertura_nascimento: FM.data_nasc.value.trim() || null,
      homepage: FM.homepage.value.trim() || null,
      email: FM.email.value.trim(),
      nome_contato: FM.nome_contato.value.trim() || null,
      contato: FM.contato.value.trim(),
      // mantém a loja existente, caso presente na lista, ou utiliza "01" por padrão
      loja: (clients.find(c => c.id === id)?.loja) || '01',
      endereco: {
        cep: FM.cep.value.trim() || null,
        logradouro: FM.logradouro.value.trim(),
        numero: FM.numero.value.trim(),
        complemento: FM.complemento.value.trim() || null,
        bairro: FM.bairro.value.trim(),
        cidade: FM.cidade.value.trim(),
        uf: FM.uf.value.trim().toUpperCase(),
        pais: FM.pais.value.trim() || 'Brasil'
      }
    };

    // Envia a alteração para o backend e aguarda a conclusão
    (async () => {
      try {
        const resp = await fetch(`/api/clientes/${id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          cache: 'no-store',
          body: JSON.stringify(updatedPayload)
        });
        if (!resp.ok) {
          let errMsg = `Erro ao atualizar cliente (status ${resp.status})`;
          try {
            const errData = await resp.json();
            errMsg = errData.message || errMsg;
          } catch (_) {}
          throw new Error(errMsg);
        }
        const updatedClient = await resp.json();
        // Atualiza lista local para refletir a alteração
        const idx = clients.findIndex(c => c.id === id);
        if (idx >= 0) {
          clients[idx] = updatedClient;
        }
        DIALOG?.close();
        render(SEARCH_INP?.value || '');
      } catch (err) {
        console.error(err);
        alert(err.message);
      }
    })();
  });

  // Excluir
  function delClient(id) {
    const c = clients.find(x => x.id === id);
    if (!c) return;
    const nomeExibido = c.nome || c.razao || '';
    if (confirm(`Excluir o cliente ${nomeExibido} (ID ${c.id})?`)) {
      // solicita exclusão ao backend
      (async () => {
        try {
        const resp = await fetch(`/api/clientes/${id}`, {
          method: 'DELETE',
          cache: 'no-store'
        });
          if (!resp.ok) {
            let errMsg = `Erro ao excluir cliente (status ${resp.status})`;
            try {
              const errData = await resp.json();
              errMsg = errData.message || errMsg;
            } catch (_) {}
            throw new Error(errMsg);
          }
          // remove localmente e atualiza a tabela
          clients = clients.filter(x => x.id !== id);
          render(SEARCH_INP?.value || '');
        } catch (err) {
          console.error(err);
          alert(err.message);
        }
      })();
    }
  }

  // Inicializa lista ao carregar DOM
  document.addEventListener('DOMContentLoaded', () => {
    fetchAllClients();
  });
})();
