import { createProgram } from 'typescript';
import { ClassDiagramBuilder } from './diagram/class-diagram.builder';

export namespace diagram {
  export const generateDiagram = (fileNames: ReadonlyArray<string>) => {
    const program = createProgram(fileNames, {});
    const diagram = new ClassDiagramBuilder(program.getTypeChecker()).addSources(program.getSourceFiles()).build();
    console.log(`diagram=${diagram}`);
    return diagram;
  };
}
