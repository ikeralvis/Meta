package es.deusto.sd.google.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.deusto.sd.google.dao.CredencialesRepository;
import es.deusto.sd.google.entity.Credenciales;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private CredencialesRepository credencialesRepository;

    public boolean validarEmail(String email){
        if(email == null || email.isEmpty()){
            return false;
        }
        Optional<Credenciales> emails = credencialesRepository.findByEmail(email);
        return emails.isPresent();
    }

    public boolean validarLogin(String email, String password){ 
        if(email == null || email.isEmpty() || password == null || password.isEmpty()){
            return false;
        }
        Optional<Credenciales> credenciales = credencialesRepository.findByEmail(email);
        if(credenciales.isPresent()){
            String pass = credenciales.get().getPassword();

            return password.equals(pass);
        }
        return false;
    }
}
