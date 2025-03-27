package org.kjw.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
/**
 * @EnableBatchProcessing 애너테이션, 스프링 배치가 제공하는 이 애너테이션은 배치 인프라스트럭처를 부트스트랩하는데 사용된다.<br>
 * 이 애너테이션이 배치 인프라스트럭처를 위한 대부분의 스프링 빈 정의를 제공하므로 다음과 같은 컴포넌트를 직접 포함시킬 필요는 없다.<br>
 * - JobRepository: 실행 중인 잡의 상태를 기록하는데 사용됨<br>
 * - JobLauncher: 잡을 구동하는 데 사용됨<br>
 * - JobExplorer: JobRepository를 사용해 읽기 전용 작업을 수행하는 데 사용됨<br>
 * - JobRegistry: 특정한 런처 구현체를 사용할 때 잡을 찾는 용도로 사용됨<br>
 * - PlatformTransactionManager: 잡 진행 과정에서 트랜잭션을 다루는데 사용됨<br>
 * - JobBuilderFactory: 잡을 생성하는 빌더<br>
 * - StepBuilderFactory: 스텝을 생성하는 빌더<br>
 * 보다시피 이 애너테이션은 많은 도움을 준다. 또한, 데이터 소스(DataSource)도 제공돼야한다는 한 가지 요구 사항이 더 있다.<br>
 * JobRepository와 PlatformTransactionManager는 필요에 따라 데이터 소스를 사용할 수도 있다.<br>
 * @SpringBootApplication 애너테이션이 있다.<br>
 * 이 애너테이션은 실제로 @ComponentScan과 @EnableAutoConfiguration을 결합한 메타 애너테이션이다.<br>
 * 이 애너테이션은 데이터 소스뿐만 아니라 스프링 부트 기반의 적절한 자동 구성을 만들어 준다.<br>
 * 클래스 정의가 끝난 이후에, 스프링 배치가 제공하는 두 개의 빌더(하나는 잡을 빌드하고 다른 하나는 스텝을 빌드함)를 자동와이어링한다.<br>
 * 각 빌더는 @EnableBatchProcessing 애너테이션을 적용함으로써 자동으로 제공되므로, 해당 애너테이션을 적용하여 스프링이 각 빌더를 주입하게 만들기만 하면 된다.<br>
 * 다음으로 스텝을 만든다. 이 잡은 단일 스텝으로 구성되므로 간단하게 스텝 이름만 지정한다.<br>
 * 스텝은 빈으로 구성됐으며, 이 간단한 예제에서는 두 가지 요소인, 이름 및 태스크릿(Tasklet)만 필요하다. 인라인으로 작성된 태스크릿은 잡 내에서 실제 일을 수행한다.<br>
 * RepeatStatus.FINISHED를 반환한다는 것은 태스크릿이 완료됐음을 스프링 배치에게 알리겠다는 의미이다. 이 밖에 RepeatStatus.CONTINUABLE을 반환할 수도 있다.<br>
 * 이 경우에 스프링 배치는 태스크릿을 다시 호출한다. 다시 수행됐을 때에도 RepeatStatus.CONTINUABLE을 반환한다면 영원히 멈추지 않고 수행한다.<br>
 * 스텝을 구성했으니, 이제 이 스텝을 사용해 잡을 작성할 수 있다. 앞에서 언급했듯이 잡은 하나 이상의 스텝으로 구성된다. 스텝을 작성할 때와 유사한 방식으로,<br>
 * JobBuilderFactory를 사용해 잡을 구성한다. 잡 이름과 해당 잡에서 시작할 스텝을 구성한다.
 */
public class SpringBatchApplication {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step step() {
		return this.stepBuilderFactory.get("step1")
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
					System.out.println("Hello, World!");
					return RepeatStatus.FINISHED;
				}
			}).build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job")
			.start(step())
			.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
