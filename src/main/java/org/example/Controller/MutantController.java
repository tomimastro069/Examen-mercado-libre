package org.example.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.Service.MutantService;
import org.example.Service.StatsService;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Tag(name = "Mutant Detector", description = "API para detección de mutantes y estadísticas")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;


    @PostMapping("/mutant")
    @Operation(summary = "Verificar si un ADN es mutante",
            description = "Analiza una secuencia de ADN y determina si pertenece a un mutante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es mutante"),
            @ApiResponse(responseCode = "403", description = "No es mutante"),
            @ApiResponse(responseCode = "400", description = "ADN inválido")
    })
    public ResponseEntity<Map<String, Object>> checkMutant(@Validated @RequestBody DnaRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean isMutant = mutantService.analyzeDna(request.getDna());

            response.put("dna", request.getDna());
            response.put("isMutant", isMutant);
            response.put("message", isMutant ? "Es mutante" : "No es mutante");

            return new ResponseEntity<>(response, isMutant ? HttpStatus.OK : HttpStatus.FORBIDDEN);

        } catch (IllegalArgumentException e) {
            response.put("error", "ADN inválido");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas",
            description = "Retorna las estadísticas de verificaciones de ADN realizadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    })
    public ResponseEntity<Map<String, Object>> getStats() {
        StatsResponse stats = statsService.getStats();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "📈 Estadísticas generales del sistema");
        response.put("cantidad de mutantes:", stats.getCountMutantDna());
        response.put("cantidad de humanos", stats.getCountHumanDna());
        response.put("ratio", stats.getRatio());

        return ResponseEntity.ok(response);
    }
}