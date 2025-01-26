package br.com.fiap.soat07.hackathon.core.exception;

public class CodigoDoArquivoInvalido extends BaseException {

    private final String codigo;

    public CodigoDoArquivoInvalido(String codigo) {
        super("Código informado ["+codigo+"] não é válido" );
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
